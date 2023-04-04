package com.raisondata.openai.images

import io.circe._
import io.circe.generic.semiauto._

import java.time.Instant

trait ImageMarshaller {
  case class DataBody(url: String)
  case class ImageResponse(created: Instant, data: List[DataBody])
  case class ImageRequest(
      prompt: String,
      n: Option[Int],
      size: Option[String],
      response_format: Option[String],
      user: Option[String]
  )

  object DataBody {
    implicit val decoder: Decoder[DataBody] = deriveDecoder[DataBody]
  }

  object ImageResponse {
    implicit val decoder: Decoder[ImageResponse] = deriveDecoder[ImageResponse]
  }

  object ImageRequest {
    implicit val encoder: Encoder[ImageRequest] = deriveEncoder[ImageRequest]
  }
}
