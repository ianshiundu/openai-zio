package com.raisondata.openai.api.files

import io.circe._
import io.circe.generic.semiauto._

trait FileMarshaller {

  case class FileResponse(
      id: Option[String],
      `object`: Option[String],
      bytes: Option[Int],
      created_at: Option[String],
      filename: Option[String],
      purpose: Option[String],
      deleted: Option[Boolean]
  )

  object FileResponse {
    implicit val decoder: Decoder[FileResponse] = deriveDecoder[FileResponse]
  }

}
