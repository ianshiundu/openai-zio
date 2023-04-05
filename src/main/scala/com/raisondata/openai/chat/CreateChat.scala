package com.raisondata.openai.chat

import com.raisondata.openai.audio.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.MediaType.ApplicationJson
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

      val request = basicRequest
        .post(uri)
        .header("Authorization", s"Bearer $openaiAPIKey")
        .contentType(ApplicationJson)
        .body(requestBody)
        .readTimeout(5.minute.asScala)
        .response(asJson[ChatResponse])

      makeRequest(request)(backend).map(_.body match {
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
