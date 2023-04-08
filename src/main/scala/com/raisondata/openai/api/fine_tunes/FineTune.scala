package com.raisondata.openai.api.fine_tunes

import com.raisondata.openai.SttpConfig

object FineTune extends SttpConfig with FineTuneMarshaller {
  override def domain: String = "fine-tunes"

  override def usage: String = ""

  def createFineTune = ???

  def listFineTunes = ???

  def retrieveFineTunes = ???

  def cancelFineTune = ???
}
