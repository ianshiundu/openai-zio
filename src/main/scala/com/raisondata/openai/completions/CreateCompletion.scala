package com.raisondata.openai.completions

import com.raisondata.openai.audio.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.MediaType.ApplicationJson
import zio._

object CreateCompletion extends SttpConfig with CompletionMarshaller {
  override def domain: String = "completions"

  override def usage: String = ""

  override val uri = uri"https://api.openai.com/v1/$domain"

  def createCompletion(
      model: String,
      prompt: String,
      user: Option[String],
      max_tokens: Int,
      temperature: Double,
      top_p: Double,
      n: Int,
      stream: Boolean,
      echo: Boolean,
      presence_penalty: Double,
      frequency_penalty: Double,
      best_of: Int,
      logit_bias: Map[String, Double],
      stop: Option[Array[String]],
      logprobs: Option[Int],
      suffix: Option[String]
  )(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, CreateCompletion.CompletionResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val requestBody = CompletionRequest(
        model,
        prompt,
        suffix,
        max_tokens,
        temperature,
        top_p,
        n,
        stream,
        logprobs,
        echo,
        stop,
        presence_penalty,
        frequency_penalty,
        best_of,
        logit_bias,
        user
      )

      val request = basicRequest
        .post(uri)
        .header("Authorization", s"Bearer $openaiAPIKey")
        .contentType(ApplicationJson)
        .body(requestBody)
        .readTimeout(5.minute.asScala)
        .response(asJson[CompletionResponse])

      makeRequest(request)(backend).map(_.body match {
        case Left(error) =>
          println(s"An error occurred while making a request $error")
          throw new RuntimeException(error)
        case Right(value) =>
          println(s"Text completion was successful!")
          println(value)
          value
      })

    }
}
