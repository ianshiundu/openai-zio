package com.raisondata.audio

import io.circe
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.MediaType.MultipartFormData
import zio._

import java.io.File

trait Transcription {
  def transcribe(
                  filePath: String,
                  prompt: Option[String],
                  responseFormat: Option[String], // Enum maybe?
                  temperature: Option[Int],
                  language: Option[String], //ISO-639-1 format
                  model: String = "whisper-1", //Enum maybe?
                ): Task[Response[Either[ResponseException[String, circe.Error], Transcription.TranscribeResponse]]]
}

object Transcription extends SttpConfig with AudioMarshaller {
  override def usage: String = "transcriptions"
  override def domain: String = "audio"

  def transcribe(
      filePath: String,
      model: String = "whisper-1", //Enum maybe?
      prompt: Option[String] = None,
      responseFormat: Option[String] = None, // Enum maybe?
      temperature: Option[Int] = None,
      language: Option[String] = None, //ISO-639-1 format
                ): ZIO[SttpClient, Throwable, TranscribeResponse] = {

    val audioFile = new File(filePath)

    val request =
      basicRequest
        .post(uri)
        .header("Authorization", s"Bearer $openaiAPIKey")
        .contentType(MultipartFormData)
        .multipartBody(Seq(
          multipartFile("file", audioFile),
          multipart("model", model),
          multipart("prompt", prompt),
          multipart("response_format", responseFormat.getOrElse("json")),
          multipart("temperature", temperature.getOrElse(0)),
          multipart("language", language.getOrElse("en"))
        ))
        .readTimeout(5.minute.asScala)
        .response(asJson[TranscribeResponse])

    send(request).map(_.body match {
      case Left(error) =>
        println(s"An error occurred while making a request $error")
        throw new RuntimeException(error)
      case Right(value) =>
        println(s"Audio was transcribed successfully!")
        println(value)
        value
    })
  }

}
