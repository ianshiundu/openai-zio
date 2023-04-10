package com.raisondata.openai

sealed trait Pixel {
  def asString: String
}

case object `256x256` extends Pixel {
  val asString = "256x256"
}

case object `512x512` extends Pixel {
  val asString = "512x512"
}

case object `1024x1024` extends Pixel {
  val asString = "1024x1024"
}
