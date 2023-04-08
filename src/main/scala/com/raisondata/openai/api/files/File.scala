package com.raisondata.openai.api.files

import com.raisondata.openai.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.Uri
import zio._

import java.io.{File => fle}

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

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Files were returned successfully!")
          } yield response
      })
    }

  def uploadFile(filePath: String, purpose: String)(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, File.FileResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val file = new fle(filePath)
      val body = Seq(multipartFile("file", file), multipart("purpose", purpose))

      val request =
        requestWithMultipartForm(body, asJson[FileResponse])(openaiAPIKey)

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
            _ <- ZIO.logInfo("File was uploaded successfully!")
          } yield response
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

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("File was deleted successfully!")
          } yield response
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

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("File was retrieved successfully!")
          } yield response
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

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("File content was retrieved successfully!")
          } yield response
      })
    }
}
