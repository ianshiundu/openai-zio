package com.raisondata.openai.chat

import com.raisondata.openai.SttpConfig
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import zio._

object CreateChat extends SttpConfig with ChatMarshaller {
  override def domain: String = "chat"

  override def usage: String = "completions"

  def createChat(
      model: String,
      messages: List[Message],
      user: Option[String],
      temperature: Double,
      top_p: Double,
      n: Int,
      stream: Boolean,
      presence_penalty: Double,
      frequency_penalty: Double,
      logit_bias: Map[String, Double],
      max_tokens: Option[Int],
      stop: Option[Array[String]]
  )(openaiAPIKey: String): ZIO[Any, Throwable, CreateChat.ChatResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val requestBody = ChatRequest(
        model,
        messages,
        user,
        max_tokens,
        temperature,
        top_p,
        n,
        stream,
        presence_penalty,
        frequency_penalty,
        logit_bias,
        stop
      )

      val request =
        requestWithJsonBody(requestBody, asJson[ChatResponse])(openaiAPIKey)
      val response = getResponse(request)(backend)

      response.map(_.body match {
        case Left(error) =>
          println(s"An error occurred while making a request $error")
          throw new RuntimeException(error)
        case Right(value) =>
          println(s"Chat reply was successful returned!")
          println(value)
          value
      })
    }
}
