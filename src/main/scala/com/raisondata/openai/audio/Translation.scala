package com.raisondata.audio

import com.raisondata.{Model, ResponseFormat}
import com.raisondata.Model.Model
import com.raisondata.ResponseFormat.ResponseFormat
import com.raisondata.helpers._
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.MediaType.MultipartFormData
import zio._

import java.io.File

object Translation extends SttpConfig with AudioMarshaller {
  override def domain: String = "audio"

  override def usage: String = "translations"

  def translate(
      filePath: String,
      model: Model,
      responseFormat: ResponseFormat,
      temperature: Int,
      prompt: Option[String] = None
  )(openaiAPIKey: String): ZIO[Any, Throwable, TextResponse] =
    HttpClientZioBackend().flatMap { backend =>
      require(
        temperature >= 0 && temperature <= 1,
        throw new IllegalArgumentException(
          "Temperature value must be between ranges 0 and 1."
        )
      )

      val audioFile = new File(filePath)

      val request =
        basicRequest
          .post(uri)
          .header("Authorization", s"Bearer $openaiAPIKey")
          .contentType(MultipartFormData)
          .multipartBody(
            Seq(
              multipartFile("file", audioFile),
              multipart("model", Model.parse(model)),
              multipart("prompt", prompt),
              multipart(
                "response_format",
                ResponseFormat.parse(responseFormat)
              ),
              multipart("temperature", temperature)
            )
          )
          .readTimeout(5.minute.asScala)
          .response(asJson[TextResponse])

      makeRequest(request)(backend).map(_.body match {
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
