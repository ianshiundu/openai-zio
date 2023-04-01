package com.raisondata.audio

import io.circe._
import io.circe.generic.semiauto._

trait AudioMarshaller {
  case class TranscribeResponse(text: String)

  object TranscribeResponse {
    implicit val decoder: Decoder[TranscribeResponse] =
      deriveDecoder[TranscribeResponse]
  }
}
