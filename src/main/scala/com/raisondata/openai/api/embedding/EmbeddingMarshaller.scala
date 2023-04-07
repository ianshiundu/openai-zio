package com.raisondata.openai.api.embedding

import io.circe._
import io.circe.generic.semiauto._

trait EmbeddingMarshaller {
  case class EmbeddingRequest(
      model: String,
      input: String,
      user: Option[String]
  )

  object EmbeddingRequest {
    implicit val encoder: Encoder[EmbeddingRequest] =
      deriveEncoder[EmbeddingRequest]
  }

  case class Data(
      `object`: String,
      embedding: List[Double],
      index: Int
  )

  case class Usage(prompt_tokens: Int, total_tokens: Int)

  case class EmbeddingResponse(
      `object`: Option[String],
      data: List[Data],
      model: String,
      usage: Usage
  )

  object Data {
    implicit val decoder: Decoder[Data] = deriveDecoder[Data]
  }

  object Usage {
    implicit val decoder: Decoder[Usage] = deriveDecoder[Usage]
  }

  object EmbeddingResponse {
    implicit val decoder: Decoder[EmbeddingResponse] =
      deriveDecoder[EmbeddingResponse]
  }
}
