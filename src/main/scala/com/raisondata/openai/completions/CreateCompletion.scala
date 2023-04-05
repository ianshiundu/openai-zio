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
      prompt: String = "<|endoftext|>",
      user: Option[String] = Some(""),
      max_tokens: Int = 16,
      temperature: Double = 1,
      top_p: Double = 1,
      n: Int = 1,
      stream: Boolean = false,
      echo: Boolean = false,
      presence_penalty: Double = 0,
      frequency_penalty: Double = 0,
      best_of: Int = 1,
      logit_bias: Map[String, Double] = Map(), // Defaults to null
      stop: Option[Array[String]] = None, // Defaults to null
      logprobs: Option[Int] = None, // Defaults to null
      suffix: Option[String] = None // Defaults to null
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
