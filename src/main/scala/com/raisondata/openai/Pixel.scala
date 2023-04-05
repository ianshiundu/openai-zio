package com.raisondata.openai

sealed trait Pixel {
  def asString: String
}

case object Px256x256 extends Pixel {
  val asString = "256x256"
}

case object Px512x512 extends Pixel {
  val asString = "512x512"
}

case object Px1024x1024 extends Pixel {
  val asString = "1024x1024"
}
