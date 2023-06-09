package com.raisondata.openai.api.edits

import com.raisondata.openai.Model.Model
import com.raisondata.openai.{Model, SttpConfig}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.circe._
import sttp.client4.{UriContext, WebSocketStreamBackend}
import zio.{Task, ZIO}

object Edit extends SttpConfig with EditMarshaller {
  override def domain: String = "edits"

  override def usage: String = ""

  override val uri = uri"$baseURL/$version/$domain"

  def createEdit(
      model: Model,
      input: String,
      instruction: String,
      n: Int,
      temperature: Double,
      top_p: Double
  )(openaiAPIKey: String)(implicit
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, Edit.EditResponse] = {
    val requestBody =
      EditRequest(Model.parse(model), input, instruction, n, temperature, top_p)

    val request =
      requestWithJsonBody(requestBody, asJson[EditResponse])(openaiAPIKey)

    val response = getResponse(request)(backend)

    response.flatMap(_.body match {
      case Left(error) =>
        for {
          _ <- ZIO.logError(s"An error occurred while making a request $error")
        } yield throw new RuntimeException(error)
      case Right(response) =>
        for {
          _ <- ZIO.logInfo("Text was edited successfully!")
        } yield response
    })
  }
}
