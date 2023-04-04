package com.raisondata

import com.raisondata.Language.Language
import com.raisondata.Model.Model
import com.raisondata.ResponseFormat.ResponseFormat
import com.raisondata.audio.{Transcription, Translation}
import zio.ZIO

/** @param apiKey
  *   OpenAI's API Key
  * @see
  *   <a
  *   href="https://platform.openai.com/docs/api-reference/authentication">Authentication:</a>
  */
class OpenAI(apiKey: String) {

  // speech to text API
  /** This is the speech to text API wrapper
    * @see
    *   <a href="https://platform.openai.com/docs/api-reference/audio">Speech to
    *   Text API</a>
    */
  object Audio {

    /** Transcribes audio into the input language.
      *
      * @param filePath
      *   Required The audio file to transcribe, in one of these formats: mp3,
      *   mp4, mpeg, mpga, m4a, wav, or webm.
      * @param model
      *   Required ID of the model to use. Only `whisper-1` is currently
      *   available.
      * @param responseFormat
      *   Defaults to JSON The format of the transcript output, in one of these
      *   options: json, text, srt, verbose_json, or vtt.
      * @param temperature
      *   Optional Defaults to 0 The sampling temperature, between 0 and 1.
      *   Higher values like 0.8 will make the output more random, while lower
      *   values like 0.2 will make it more focused and deterministic.
      * @param language
      *   The language of the input audio. Supplying the input language in
      *   ISO-639-1 format will improve accuracy and latency.
      * @param prompt
      *   An optional text to guide the model's style or continue a previous
      *   audio segment. The prompt should be in English.
      *
      * @see
      *   <a
      *   href="https://platform.openai.com/docs/api-reference/audio/create">Create
      *   transcription</a>
      */
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

    /** Translates audio into into English.
      *
      * @param filePath
      *   Required The audio file to transcribe, in one of these formats: mp3,
      *   mp4, mpeg, mpga, m4a, wav, or webm.
      * @param model
      *   Required ID of the model to use. Only `whisper-1` is currently
      *   available.
      * @param responseFormat
      *   Defaults to JSON The format of the transcript output, in one of these
      *   options: json, text, srt, verbose_json, or vtt.
      * @param temperature
      *   Optional Defaults to 0 The sampling temperature, between 0 and 1.
      *   Higher values like 0.8 will make the output more random, while lower
      *   values like 0.2 will make it more focused and deterministic.
      * @param prompt
      *   An optional text to guide the model's style or continue a previous
      *   audio segment. The prompt should be in English.
      * @see
      *   <a
      *   href="https://platform.openai.com/docs/api-reference/audio/create">Create
      *   translation</a>
      */

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
        prompt
      )(apiKey)
  }

}
