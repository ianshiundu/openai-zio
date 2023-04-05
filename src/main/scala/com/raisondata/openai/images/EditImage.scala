package com.raisondata.openai.images

import com.raisondata.openai.ResponseFormat.ResponseFormat
import com.raisondata.openai.audio.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import com.raisondata.openai.{Pixel, Px1024x1024, ResponseFormat}
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.MediaType.MultipartFormData
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

      val request = basicRequest
        .post(uri)
        .header("Authorization", s"Bearer $openaiAPIKey")
        .contentType(MultipartFormData)
        .multipartBody(
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
        .readTimeout(5.minute.asScala)
        .response(asJson[ImageResponse])

      makeRequest(request)(backend)
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
