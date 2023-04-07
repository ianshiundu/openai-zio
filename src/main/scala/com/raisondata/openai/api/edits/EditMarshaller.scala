package com.raisondata.openai.api.edits

import io.circe._
import io.circe.generic.semiauto._

trait EditMarshaller {
  case class EditRequest(
      model: String,
      input: String,
      instruction: String,
      n: Int,
      temperature: Double,
      top_p: Double
  )

  object EditRequest {
    implicit val encoder: Encoder[EditRequest] = deriveEncoder[EditRequest]
  }

  case class Choices(
      text: String,
      index: Int
  )

  case class Usage(
      prompt_tokens: Int,
      completion_tokens: Int,
      total_tokens: Int
  )

  case class EditResponse(
      `object`: String,
      choices: List[Choices],
      usage: Usage
  )

  object Choices {
    implicit val decoder: Decoder[Choices] = deriveDecoder[Choices]
  }

  object Usage {
    implicit val decoder: Decoder[Usage] = deriveDecoder[Usage]
  }

  object EditResponse {
    implicit val decoder: Decoder[EditResponse] =
      deriveDecoder[EditResponse]
  }

}
