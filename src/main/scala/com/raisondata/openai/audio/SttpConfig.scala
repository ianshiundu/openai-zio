package com.raisondata.audio

import sttp.client4.UriContext

trait SttpConfig {
  def usage: String
  def domain: String

  private val baseURL = "https://api.openai.com"

  private val version = "v1"

  val uri = uri"$baseURL/$version/$domain/$usage"

}
