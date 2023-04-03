package com.raisondata

object ResponseFormat extends Enumeration {
  type ResponseFormat = Value

  val json, text, srt, verbose_json, vtt, url, b64_json = Value

  def parse(format: ResponseFormat): String = format.toString
}
