package com.raisondata.openai

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

object Role extends Enumeration {
  type Role = Value

  val system, user, assistant = Value

  def parse(role: Role): String = role.toString

  implicit val encoder: Encoder[Role] =
    Encoder.encodeEnumeration(Role)

  implicit val decoder: Decoder[Role] =
    Decoder.decodeEnumeration(Role)
}
