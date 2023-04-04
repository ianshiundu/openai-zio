package com.raisondata.openai.audio

import io.circe._
import io.circe.generic.semiauto._

trait AudioMarshaller {
  case class TextResponse(text: String)

  object TextResponse {
    implicit val decoder: Decoder[TextResponse] =
      deriveDecoder[TextResponse]
  }
}
