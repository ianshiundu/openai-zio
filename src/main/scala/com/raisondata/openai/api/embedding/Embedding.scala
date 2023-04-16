package com.raisondata.openai.api.embedding

import com.raisondata.openai.Model.Model
import com.raisondata.openai.{Model, SttpConfig}
import sttp.capabilities.zio.ZioStreams
import sttp.client4._
import sttp.client4.circe._
import sttp.model.Uri
import zio.{Task, ZIO}

object Embedding extends SttpConfig with EmbeddingMarshaller {
  override def domain: String = "embeddings"

  override def usage: String = ""

  override val uri: Uri = uri"$baseURL/$version/$domain"

  def createEmbeddings(model: Model, input: String, user: Option[String])(
      openaiAPIKey: String
  )(implicit
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, Embedding.EmbeddingResponse] = {
      val requestBody = EmbeddingRequest(Model.parse(model), input, user)

      val request =
        requestWithJsonBody(requestBody, asJson[EmbeddingResponse])(
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
            _ <- ZIO.logInfo("Embeddings were successfully created!")
          } yield response
      })
    }
}
