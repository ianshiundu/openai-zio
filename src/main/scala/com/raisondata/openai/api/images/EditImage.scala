package com.raisondata.openai.api.images

import com.raisondata.openai.ResponseFormat.ResponseFormat
import com.raisondata.openai._
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import zio._

import java.io.File

object EditImage extends SttpConfig with ImageMarshaller {
  override def domain: String = "images"

  override def usage: String = "edits"

  def editImage(
      imageToEditPath: String,
      prompt: String,
      size: Pixel,
      responseFormat: ResponseFormat,
      imageMaskPath: Option[String],
      numberOfImages: Option[Int],
      user: Option[String]
  )(openaiAPIKey: String): ZIO[Any, Throwable, EditImage.ImageResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val imageFile = new File(imageToEditPath)

      val requestBody = Seq(
        multipartFile("image", imageFile),
        imageMaskPath match {
          case Some(path) => multipartFile("mask", new File(path))
          case None       => multipart("mask", None)
        },
        multipart("prompt", prompt),
        multipart("n", numberOfImages),
        multipart("size", size.asString),
        multipart(
          "response_format",
          ResponseFormat.parse(responseFormat)
        ),
        multipart("user", user)
      )

      val request =
        requestWithMultipartForm(requestBody, asJson[ImageResponse])(
          openaiAPIKey
        )
      val response = getResponse(request)(backend)

      response
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"Image was edited successfully!")
            println(value)
            value
        })
    }

}
