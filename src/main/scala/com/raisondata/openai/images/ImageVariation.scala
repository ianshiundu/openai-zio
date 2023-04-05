package com.raisondata.openai.images

import com.raisondata.openai.ResponseFormat.ResponseFormat
import com.raisondata.openai.audio.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import com.raisondata.openai.{Pixel, ResponseFormat}
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

      val request = basicRequest
        .post(uri)
        .header("Authorization", s"Bearer $openaiAPIKey")
        .contentType(MultipartFormData)
        .multipartBody(
          multipartFile("image", imageFile),
          multipart("size", size.asString),
          multipart("response_format", ResponseFormat.parse(responseFormat)),
          multipart("n", numberOfImages),
          multipart("user", user)
        )
        .readTimeout(5.minute.asScala)
        .response(asJson[ImageResponse])

      makeRequest(request)(backend)
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"Image variation was processed successfully!")
            println(value)
            value
        })
    }

}
