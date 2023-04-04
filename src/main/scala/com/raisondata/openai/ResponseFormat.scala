package com.raisondata.openai

/** Response Enums supported by the Open AI endpoints.
  * @see
  *   <a
  *   href="https://platform.openai.com/docs/api-reference/audio/create#audio/create-response_format">Response
  *   Format</a>
  */
object ResponseFormat extends Enumeration {
  type ResponseFormat = Value

  val json, text, srt, verbose_json, vtt, url, b64_json = Value

  def parse(format: ResponseFormat): String = format.toString
}
