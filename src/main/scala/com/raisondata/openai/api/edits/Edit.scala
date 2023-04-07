package com.raisondata.openai.api.edits

import com.raisondata.openai.SttpConfig
import sttp.client4.UriContext
import sttp.client4.circe._
import sttp.client4.httpclient.zio.HttpClientZioBackend
import zio.ZIO

object Edit extends SttpConfig with EditMarshaller {
  override def domain: String = "edits"

  override def usage: String = ""

  override val uri = uri"$baseURL/$version/$domain"

  def createEdit(
      model: String,
      input: String,
      instruction: String,
      n: Int,
      temperature: Double,
      top_p: Double
  )(openaiAPIKey: String): ZIO[Any, Throwable, Edit.EditResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val requestBody =
        EditRequest(model, input, instruction, n, temperature, top_p)

      val request =
        requestWithJsonBody(requestBody, asJson[EditResponse])(openaiAPIKey)

      val response = getResponse(request)(backend)

      response
        .map(_.body match {
          case Left(error) =>
            println(s"An error occurred while making a request $error")
            throw new RuntimeException(error)
          case Right(value) =>
            println(s"Text was edited successfully!")
            println(value)
            value
        })
    }
}
