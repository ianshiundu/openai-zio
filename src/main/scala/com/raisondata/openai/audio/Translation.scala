package com.raisondata.openai.audio

import com.raisondata.openai.Model.Model
import com.raisondata.openai.ResponseFormat.ResponseFormat
import com.raisondata.openai._
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
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

      val body = Seq(
        multipartFile("file", audioFile),
        multipart("model", Model.parse(model)),
        multipart("prompt", prompt),
        multipart(
          "response_format",
          ResponseFormat.parse(responseFormat)
        ),
        multipart("temperature", temperature)
      )

      val request =
        requestWithMultipartForm(body, asJson[TextResponse])(openaiAPIKey)
      val response = getResponse(request)(backend)

      response.map(_.body match {
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
