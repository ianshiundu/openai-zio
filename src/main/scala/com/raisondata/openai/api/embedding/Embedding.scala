package com.raisondata.openai.api.embedding

import com.raisondata.openai.SttpConfig
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.Uri
import zio.ZIO

object Embedding extends SttpConfig with EmbeddingMarshaller {
  override def domain: String = "embeddings"

  override def usage: String = ""

  override val uri: Uri = uri"$baseURL/$version/$domain"

  def createEmbeddings(model: String, input: String, user: Option[String])(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, Embedding.EmbeddingResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val requestBody = EmbeddingRequest(model, input, user)

      val request =
        requestWithJsonBody(requestBody, asJson[EmbeddingResponse])(
          openaiAPIKey
        )

      val response = getResponse(request)(backend)

      response.map(_.body match {
        case Left(error) =>
          println(s"An error occurred while making a request $error")
          throw new RuntimeException(error)
        case Right(value) =>
          println(s"Embeddings were successfully created!")
          println(value)
          value
      })
    }
}
