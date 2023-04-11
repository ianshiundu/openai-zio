package com.raisondata.openai

object Role extends Enumeration {
  type Role = Value

  val system, user, assistant = Value

  def parse(role: Role): String = role.toString

}
