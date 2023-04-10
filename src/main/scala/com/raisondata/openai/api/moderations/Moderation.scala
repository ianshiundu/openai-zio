package com.raisondata.openai.api.moderations

import com.raisondata.openai.Model._
import com.raisondata.openai.{Model, SttpConfig}
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.Uri
import zio.ZIO

object Moderation extends SttpConfig with ModerationMarshaller {
  override def domain: String = "moderations"

  override def usage: String = ""

  override val uri: Uri = uri"$baseURL/$version/$domain"

  def createModeration(input: String, model: Model)(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, Moderation.ModerationResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val requestBody = ModerationRequest(input, Model.parse(model))

      val request =
        requestWithJsonBody(requestBody, asJson[ModerationResponse])(
          openaiAPIKey
        )

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
            _ <- ZIO.logInfo(
              "Text moderation results were returned successfully!"
            )
          } yield response
      })
    }
}
