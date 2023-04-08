package com.raisondata.openai.api.files

import com.raisondata.openai.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.Uri
import zio._

import java.io.File

object File extends SttpConfig with FileMarshaller {
  override def domain: String = "files"

  override def usage: String = ""

  override val uri: Uri = uri"$baseURL/$version/$domain"

  def listFiles(openaiAPIKey: String): ZIO[Any, Throwable, File.FileResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .get(uri)
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[FileResponse])

      makeRequest(request)(backend)
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"Files were returned successfully!")
            println(value)
            value
        })
    }

  def uploadFile(filePath: String, purpose: String)(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, File.FileResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val file = new File(filePath)
      val body = Seq(multipartFile("file", file), multipart("purpose", purpose))

      val request =
        requestWithMultipartForm(body, asJson[FileResponse])(openaiAPIKey)

      val response = getResponse(request)(backend)

      response
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"Files were uploaded successfully!")
            println(value)
            value
        })

    }

  def deleteFile(
      fileId: String
  )(openaiAPIKey: String): ZIO[Any, Throwable, File.FileResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .delete(uri"$uri/$fileId")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[FileResponse])

      makeRequest(request)(backend)
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"File was deleted successfully!")
            println(value)
            value
        })
    }

  def retrieveFile(fileId: String)(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, File.FileResponse] = HttpClientZioBackend().flatMap {
    backend =>
      val request = basicRequest
        .get(uri"$uri/$fileId")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[FileResponse])

      makeRequest(request)(backend)
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"Files was retrieved successfully!")
            println(value)
            value
        })
  }

  // TODO Add FileContent response - calling this should fail
  def retrieveFileContent(
      fileId: String
  )(openaiAPIKey: String): ZIO[Any, Throwable, File.FileResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .get(uri"$uri/$fileId/content")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[FileResponse])

      makeRequest(request)(backend)
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"File content was retrieved successfully!")
            println(value)
            value
        })
    }
}
