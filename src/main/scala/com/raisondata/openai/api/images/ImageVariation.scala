package com.raisondata.openai.api.images

import com.raisondata.openai.ResponseFormat.ResponseFormat
import com.raisondata.openai.helpers.makeRequest
import com.raisondata.openai.{Pixel, ResponseFormat, SttpConfig}
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.MediaType.MultipartFormData
import zio._

import java.io.File

object ImageVariation extends SttpConfig with ImageMarshaller {
  override def domain: String = "images"

  override def usage: String = "variations"

  def getVariations(
      imagePath: String,
      size: Pixel,
      responseFormat: ResponseFormat,
      numberOfImages: Option[Int],
      user: Option[String]
  )(openaiAPIKey: String): ZIO[Any, Throwable, ImageVariation.ImageResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val imageFile = new File(imagePath)

      val body = Seq(
        multipartFile("image", imageFile),
        multipart("size", size.asString),
        multipart("response_format", ResponseFormat.parse(responseFormat)),
        multipart("n", numberOfImages),
        multipart("user", user)
      )

      val request =
        requestWithMultipartForm(body, asJson[ImageResponse])(openaiAPIKey)
      val response = getResponse(request)(backend)

      response.flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Image variation was processed successfully!")
          } yield response
      })
    }

}
