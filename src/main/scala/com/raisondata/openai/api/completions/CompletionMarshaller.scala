package com.raisondata.openai.api.completions

import io.circe._
import io.circe.generic.semiauto._

trait CompletionMarshaller {
  case class CompletionRequest(
      model: String,
      prompt: String,
      suffix: Option[String],
      max_tokens: Int,
      temperature: Double,
      top_p: Double,
      n: Int,
      stream: Boolean,
      logprobs: Option[Int],
      echo: Boolean,
      stop: Option[Array[String]],
      presence_penalty: Double,
      frequency_penalty: Double,
      best_of: Int,
      logit_bias: Map[String, Double],
      user: Option[String]
  )

  object CompletionRequest {
    implicit val encoder: Encoder[CompletionRequest] =
      deriveEncoder[CompletionRequest]
  }

  case class Choices(
      text: String,
      index: Int,
      finish_reason: String,
      logprobs: Option[Int]
  )

  case class Usage(
      prompt_tokens: Int,
      completion_tokens: Int,
      total_tokens: Int
  )

  case class CompletionResponse(
      id: String,
      `object`: String,
      model: String,
      choices: List[Choices],
      usage: Usage
  )

  object Choices {
    implicit val decoder: Decoder[Choices] = deriveDecoder[Choices]
  }

  object Usage {
    implicit val decoder: Decoder[Usage] = deriveDecoder[Usage]
  }

  object CompletionResponse {
    implicit val decoder: Decoder[CompletionResponse] =
      deriveDecoder[CompletionResponse]
  }
}
