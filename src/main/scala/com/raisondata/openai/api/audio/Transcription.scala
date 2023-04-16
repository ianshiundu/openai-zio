package com.raisondata.openai.api.audio

import com.raisondata.openai.Language.Language
import com.raisondata.openai.Model.Model
import com.raisondata.openai.ResponseFormat.ResponseFormat
import com.raisondata.openai._
import sttp.capabilities.zio.ZioStreams
import sttp.client4._
import sttp.client4.circe._
import zio._

import java.io.File

object Transcription extends SttpConfig with AudioMarshaller {
  override def usage: String = "transcriptions"
  override def domain: String = "audio"

  def transcribe(
      filePath: String,
      model: Model,
      responseFormat: ResponseFormat,
      temperature: Int,
      language: Language,
      prompt: Option[String] = None
  )(openaiAPIKey: String)(implicit
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, TextResponse] = {
    require(
      temperature >= 0 && temperature <= 1,
      throw new IllegalArgumentException(
        "Temperature value must be between ranges 0 and 1."
      )
    )

    val audioFile = new File(filePath)

    val body = Seq(
      multipartFile("file", audioFile),
      multipart("model", Model.parse(model)),
      multipart("prompt", prompt),
      multipart(
        "response_format",
        ResponseFormat.parse(responseFormat)
      ),
      multipart("temperature", temperature),
      multipart(
        "language",
        Language.parse(language)
      )
    )

    val request =
      requestWithMultipartForm(body, asJson[TextResponse])(openaiAPIKey)
    val response = getResponse(request)(backend)

    response.flatMap(_.body match {
      case Left(error) =>
        for {
          _ <- ZIO.logError(s"An error occurred while making a request $error")
        } yield throw new RuntimeException(error)
      case Right(response) =>
        for {
          _ <- ZIO.logInfo("Audio was transcribed successfully!")
        } yield response
    })
  }

}
