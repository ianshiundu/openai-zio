package com.raisondata

import com.raisondata.Language.Language
import com.raisondata.Model.Model
import com.raisondata.ResponseFormat.ResponseFormat
import com.raisondata.audio.{Transcription, Translation}
import zio.ZIO

class OpenAI(apiKey: String) {

  object Audio {
    def transcribe(
        filePath: String,
        model: Model = Model.whisper_1,
        responseFormat: ResponseFormat = ResponseFormat.json,
        temperature: Int = 0,
        language: Language = Language.en,
        prompt: Option[String] = None
    ): ZIO[Any, Throwable, Transcription.TextResponse] =
      Transcription.transcribe(
        filePath,
        model,
        responseFormat,
        temperature,
        language,
        prompt
      )(apiKey)

    def translate(
        filePath: String,
        model: Model = Model.whisper_1,
        responseFormat: ResponseFormat = ResponseFormat.json,
        temperature: Int = 0,
        prompt: Option[String] = None
    ): ZIO[Any, Throwable, Translation.TextResponse] =
      Translation.translate(
        filePath,
        model,
        responseFormat,
        temperature,
        prompt,
      )(apiKey)
  }

}
