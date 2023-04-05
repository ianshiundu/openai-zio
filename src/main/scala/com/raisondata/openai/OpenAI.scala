package com.raisondata.openai

import Language.Language
import Model.Model
import ResponseFormat.ResponseFormat
import com.raisondata.openai.audio.{Transcription, Translation}
import com.raisondata.openai.images.{EditImage, GenerateImage, ImageVariation}
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

  /** Given a prompt and/or an input image, the model will generate a new image.
    * @see
    *   <a
    *   href="https://platform.openai.com/docs/api-reference/images">Images</a>
    */
  object Images {

    /** Creates an image given a prompt.
      *
      * @param prompt
      *   Required A text description of the desired image(s). The maximum
      *   length is 1000 characters.
      * @param user
      *   A unique identifier representing your end-user, which can help OpenAI
      *   to monitor and detect abuse.
      * @param size
      *   Defaults to 1024x1024 The size of the generated images. Must be one of
      *   Px256x256, Px512x512, or Px1024x1024
      * @param responseFormat
      *   Optional Defaults to url The format in which the generated images are
      *   returned. Must be one of url or b64_json.
      * @param numberOfImages
      *   Optional Defaults to 1 The number of images to generate. Must be
      *   between 1 and 10.
      * @see
      *   <a
      *   href="https://platform.openai.com/docs/api-reference/images/create">Create
      *   Image</a>
      */
    def generateImage(
        prompt: String,
        user: Option[String],
        size: Pixel = Px1024x1024,
        responseFormat: ResponseFormat = ResponseFormat.url,
        numberOfImages: Option[Int] = Some(1)
    ): ZIO[Any, Throwable, GenerateImage.ImageResponse] =
      GenerateImage.generateImage(
        prompt,
        size,
        responseFormat,
        user,
        numberOfImages
      )(apiKey)

    /** Creates an edited or extended image given an original image and a
      * prompt.
      *
      * @param prompt
      *   Required A text description of the desired image(s). The maximum
      *   length is 1000 characters.
      * @param user
      *   A unique identifier representing your end-user, which can help OpenAI
      *   to monitor and detect abuse.
      * @param size
      *   Defaults to 1024x1024 The size of the generated images. Must be one of
      *   Px256x256, Px512x512, or Px1024x1024
      * @param responseFormat
      *   Optional Defaults to url The format in which the generated images are
      *   returned. Must be one of url or b64_json.
      * @param numberOfImages
      *   Optional Defaults to 1 The number of images to generate. Must be
      *   between 1 and 10.
      * @param imageToEditPath
      *   Required The image to edit. Must be a valid PNG file, less than 4MB,
      *   and square. If mask is not provided, image must have transparency,
      *   which will be used as the mask.
      * @param imageMaskPath
      *   An additional image whose fully transparent areas (e.g. where alpha is
      *   zero) indicate where image should be edited. Must be a valid PNG file,
      *   less than 4MB, and have the same dimensions as image.
      * @see
      *   <a
      *   href="https://platform.openai.com/docs/api-reference/images/create-edit">Create
      *   image edit</a>
      */
    def editImage(
        imageToEditPath: String,
        prompt: String,
        user: Option[String],
        size: Pixel = Px1024x1024,
        responseFormat: ResponseFormat = ResponseFormat.url,
        imageMaskPath: Option[String] = None,
        numberOfImages: Option[Int] = Some(1) // default 1
    ): ZIO[Any, Throwable, EditImage.ImageResponse] =
      EditImage.editImage(
        imageToEditPath,
        prompt,
        size,
        responseFormat,
        imageMaskPath,
        numberOfImages,
        user
      )(apiKey)

    /** Creates a variation of a given image.
      *
      * @param imagePath
      *   Required The image to edit. Must be a valid PNG file, less than 4MB,
      *   and square. If mask is not provided, image must have transparency,
      *   which will be used as the mask.
      * @param user
      *   A unique identifier representing your end-user, which can help OpenAI
      *   to monitor and detect abuse.
      * @param size
      *   Defaults to 1024x1024 The size of the generated images. Must be one of
      *   Px256x256, Px512x512, or Px1024x1024
      * @param responseFormat
      *   Optional Defaults to url The format in which the generated images are
      *   returned. Must be one of url or b64_json.
      * @param numberOfImages
      *   Optional Defaults to 1 The number of images to generate. Must be
      *   between 1 and 10.
      * @see
      *   <a
      *   href="https://platform.openai.com/docs/api-reference/images/create-variation">Create
      *   image variation</a>
      */
    def getVariations(
        imagePath: String,
        user: Option[String],
        size: Pixel = Px1024x1024,
        responseFormat: ResponseFormat = ResponseFormat.url,
        numberOfImages: Option[Int] = Some(1) // default 1
    ): ZIO[Any, Throwable, ImageVariation.ImageResponse] =
      ImageVariation.getVariations(
        imagePath,
        size,
        responseFormat,
        numberOfImages,
        user
      )(apiKey)

  }

}
