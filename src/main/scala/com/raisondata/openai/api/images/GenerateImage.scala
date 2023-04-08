package com.raisondata.openai.api.images

import com.raisondata.openai.ResponseFormat.ResponseFormat
import com.raisondata.openai._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import zio._

object GenerateImage extends SttpConfig with ImageMarshaller {
  override def domain: String = "images"

  override def usage: String = "generations"

  def generateImage(
      prompt: String,
      size: Pixel,
      responseFormat: ResponseFormat,
      user: Option[String],
      numberOfImages: Option[Int]
  )(openaiAPIKey: String): ZIO[Any, Throwable, GenerateImage.ImageResponse] =
    HttpClientZioBackend().flatMap { backend =>
      require(
        responseFormat == ResponseFormat.b64_json || responseFormat == ResponseFormat.url,
        throw new IllegalArgumentException(
          "Invalid response format. Image generation only accepts url or b64_json format"
        )
      )

      val requestBody = ImageRequest(
        prompt,
        numberOfImages,
        Some(size.asString),
        Some(ResponseFormat.parse(responseFormat)),
        user
      )

      val request =
        requestWithJsonBody(requestBody, asJson[ImageResponse])(openaiAPIKey)
      val response = getResponse(request)(backend)

      response.flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(s"An error occurred while making a request $error")
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Image was generated successfully!")
          } yield response
      })
    }

}
