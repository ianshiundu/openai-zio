package com.raisondata.openai.api.chat

import com.raisondata.openai.Role.Role
import io.circe._
import io.circe.generic.semiauto._

trait ChatMarshaller {
  case class Message(role: Role, content: String)

  case class ChatRequest(
      model: String,
      messages: List[Message],
      user: Option[String],
      max_tokens: Option[Int],
      temperature: Double,
      top_p: Double,
      n: Int,
      stream: Boolean,
      presence_penalty: Double,
      frequency_penalty: Double,
      logit_bias: Map[String, Double],
      stop: Option[Array[String]]
  )

  object Message {
    implicit val encoder: Encoder[Message] = deriveEncoder[Message]
    implicit val decoder: Decoder[Message] = deriveDecoder[Message]
  }

  object ChatRequest {
    implicit val encoder: Encoder[ChatRequest] = deriveEncoder[ChatRequest]
  }

  case class Choices(
      index: Int,
      message: Message,
      finish_reason: String
  )

  case class Usage(
      prompt_tokens: Int,
      completion_tokens: Int,
      total_tokens: Int
  )

  case class ChatResponse(
      id: String,
      `object`: String,
      created: Long,
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

  object ChatResponse {
    implicit val decoder: Decoder[ChatResponse] = deriveDecoder[ChatResponse]
  }
}
