package com.raisondata.openai

import Language.Language
import Model.Model
import ResponseFormat.ResponseFormat
import com.raisondata.openai.api.audio.{Transcription, Translation}
import com.raisondata.openai.api.chat.CreateChat
import com.raisondata.openai.api.completions.CreateCompletion
import com.raisondata.openai.api.edits.Edit
import com.raisondata.openai.api.embedding.Embedding
import com.raisondata.openai.api.images.{
  EditImage,
  GenerateImage,
  ImageVariation
}
import com.raisondata.openai.api.models.OpenAIModels
import com.raisondata.openai.api.moderations.Moderation
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

  /** List and describe the various models available in the API.
    * @see
    *   <a href="https://platform.openai.com/docs/models">Available Models</a>
    */
  object Models {

    /** Lists the currently available models, and provides basic information
      * about each one such as the owner and availability.
      */
    def listModels: ZIO[Any, Throwable, OpenAIModels.ListModels] =
      OpenAIModels.listModels(apiKey)

    /** Retrieves a model instance, providing basic information about the model
      * such as the owner and permissions.
      * @param model
      *   Required The ID of the model to use for this request
      */
    def getModel(model: String): ZIO[Any, Throwable, OpenAIModels.Model] =
      OpenAIModels.getModel(model)(apiKey)
  }

  /** Given a prompt, the model will return one or more predicted completions,
    * and can also return the probabilities of alternative tokens at each
    * position.
    *
    * @see
    *   <a
    *   href="https://platform.openai.com/docs/api-reference/completions">Completions</a>
    */
  object Completions {

    /** Creates a completion for the provided prompt and parameters
      * @param model
      *   Required ID of the model to use. You can use `Models.listModels` to
      *   see all available models
      * @param prompt
      *   The prompt(s) to generate completions for, encoded as a string.
      * @param user
      *   A unique identifier representing your end-user, which can help OpenAI
      *   to monitor and detect abuse
      * @param max_tokens
      *   The maximum number of tokens to generate in the completion.
      * @param temperature
      *   What sampling temperature to use, between 0 and 2. Higher values like
      *   0.8 will make the output more random, while lower values like 0.2 will
      *   make it more focused and deterministic.
      * @param top_p
      *   An alternative to sampling with temperature, called nucleus sampling,
      *   where the model considers the results of the tokens with top_p
      *   probability mass.
      * @param n
      *   How many completions to generate for each prompt.
      * @param stream
      *   Whether to stream back partial progress. If set, tokens will be sent
      *   as data-only server-sent events as they become available, with the
      *   stream terminated by a data: [DONE] message.
      * @param echo
      *   Echo back the prompt in addition to the completion.
      * @param presence_penalty
      *   Number between -2.0 and 2.0. Positive values penalize new tokens based
      *   on whether they appear in the text so far, increasing the model's
      *   likelihood to talk about new topics.
      * @param frequency_penalty
      *   Number between -2.0 and 2.0. Positive values penalize new tokens based
      *   on their existing frequency in the text so far, decreasing the model's
      *   likelihood to repeat the same line verbatim.
      * @param best_of
      *   Generates best_of completions server-side and returns the "best" (the
      *   one with the highest log probability per token). Results cannot be
      *   streamed.
      * @param logit_bias
      *   Modify the likelihood of specified tokens appearing in the completion.
      * @param logprobs
      *   Include the log probabilities on the logprobs most likely tokens, as
      *   well the chosen tokens.
      * @param suffix
      *   The suffix that comes after a completion of inserted text.
      */
    def createCompletion(
        model: String, // update this later to use Model ADT
        prompt: String = "<|endoftext|>",
        user: Option[String],
        max_tokens: Int = 16,
        temperature: Double = 1,
        top_p: Double = 1,
        n: Int = 1,
        stream: Boolean = false,
        echo: Boolean = false,
        presence_penalty: Double = 0,
        frequency_penalty: Double = 0,
        best_of: Int = 1,
        logit_bias: Map[String, Double] = Map(),
        stop: Option[Array[String]] = None,
        logprobs: Option[Int] = None,
        suffix: Option[String] = None
    ): ZIO[Any, Throwable, CreateCompletion.CompletionResponse] =
      CreateCompletion.createCompletion(
        model,
        prompt,
        user,
        max_tokens,
        temperature,
        top_p,
        n,
        stream,
        echo,
        presence_penalty,
        frequency_penalty,
        best_of,
        logit_bias,
        stop,
        logprobs,
        suffix
      )(apiKey)
  }

  /** Given a chat conversation, the model will return a chat completion
    * response.
    *
    * @see
    *   <a href="https://platform.openai.com/docs/api-reference/chat">Chat</a>
    */
  object Chat {

    /** Creates a completion for the provided prompt and parameters
      *
      * @param model
      *   Required ID of the model to use. You can use `Models.listModels` to
      *   see all available models
      * @param messages
      *   The messages to generate chat completions for, in the <a
      *   href="https://platform.openai.com/docs/guides/chat/introduction">chat
      *   format</a>
      * @param user
      *   A unique identifier representing your end-user, which can help OpenAI
      *   to monitor and detect abuse
      * @param max_tokens
      *   The maximum number of tokens to generate in the chat completion.
      * @param temperature
      *   What sampling temperature to use, between 0 and 2. Higher values like
      *   0.8 will make the output more random, while lower values like 0.2 will
      *   make it more focused and deterministic.
      * @param top_p
      *   An alternative to sampling with temperature, called nucleus sampling,
      *   where the model considers the results of the tokens with top_p
      *   probability mass.
      * @param n
      *   How many chat completion choices to generate for each input message.
      * @param stream
      *   Whether to stream back partial progress. If set, tokens will be sent
      *   as data-only server-sent events as they become available, with the
      *   stream terminated by a data: [DONE] message.
      * @param presence_penalty
      *   Number between -2.0 and 2.0. Positive values penalize new tokens based
      *   on whether they appear in the text so far, increasing the model's
      *   likelihood to talk about new topics.
      * @param frequency_penalty
      *   Number between -2.0 and 2.0. Positive values penalize new tokens based
      *   on their existing frequency in the text so far, decreasing the model's
      *   likelihood to repeat the same line verbatim.
      * @param logit_bias
      *   Modify the likelihood of specified tokens appearing in the completion.
      */
    def createChat(
        model: String, // update this later to use Model ADT
        messages: List[CreateChat.Message],
        user: Option[String],
        temperature: Double = 1,
        top_p: Double = 1,
        n: Int = 1,
        stream: Boolean = false,
        presence_penalty: Double = 0,
        frequency_penalty: Double = 0,
        logit_bias: Map[String, Double] = Map(),
        max_tokens: Option[Int] = None,
        stop: Option[Array[String]] = None
    ): ZIO[Any, Throwable, CreateChat.ChatResponse] = CreateChat.createChat(
      model,
      messages,
      user,
      temperature,
      top_p,
      n,
      stream,
      presence_penalty,
      frequency_penalty,
      logit_bias,
      max_tokens,
      stop
    )(apiKey)
  }

  /** Given a prompt and an instruction, the model will return an edited version
    * of the prompt.
    * @see
    *   <a href="https://platform.openai.com/docs/api-reference/edits">Edits</a>
    */
  object Edits {

    /** Creates a new edit for the provided input, instruction, and parameters.
      * @param model
      *   Required ID of the model to use. You can use `Models.listModels` to
      *   see all available models
      * @param input
      *   The input text to use as a starting point for the edit.
      * @param instruction
      *   The instruction that tells the model how to edit the prompt.
      * @param temperature
      *   What sampling temperature to use, between 0 and 2. Higher values like
      *   0.8 will make the output more random, while lower values like 0.2 will
      *   make it more focused and deterministic.
      * @param top_p
      *   An alternative to sampling with temperature, called nucleus sampling,
      *   where the model considers the results of the tokens with top_p
      *   probability mass.
      * @param n
      *   How many chat completion choices to generate for each input message.
      */
    def createEdit(
        model: String,
        input: String,
        instruction: String,
        n: Int = 1,
        temperature: Double = 1,
        top_p: Double = 1
    ): ZIO[Any, Throwable, Edit.EditResponse] =
      Edit.createEdit(model, input, instruction, n, temperature, top_p)(apiKey)
  }

  /** Get a vector representation of a given input that can be easily consumed
    * by machine learning models and algorithms.
    * @see
    *   <a
    *   href="https://platform.openai.com/docs/api-reference/embeddings">Embeddings</a>
    */
  object Embeddings {

    /** Creates an embedding vector representing the input text.
      * @param model
      *   ID of the model to use.
      * @param input
      *   Input text to get embeddings for, encoded as a string.
      * @param user
      *   A unique identifier representing your end-user, which can help OpenAI
      *   to monitor and detect abuse.
      */
    def createEmbeddings(
        model: String,
        input: String,
        user: Option[String]
    ): ZIO[Any, Throwable, Embedding.EmbeddingResponse] =
      Embedding.createEmbeddings(model, input, user)(apiKey)
  }

  /** Given a input text, outputs if the model classifies it as violating
    * OpenAI's content policy.
    *
    * @see
    *   <a
    *   href="https://platform.openai.com/docs/api-reference/moderations">Moderations</a>
    */
  object Moderations {

    /** Classifies if text violates OpenAI's Content Policy
      * @param input
      *   The input text to classify
      * @param model
      *   Two content moderations models are available: `text-moderation-stable`
      *   and `text-moderation-latest`
      */
    def createModeration(
        input: String,
        model: Model = Model.text_moderation_latest
    ): ZIO[Any, Throwable, Moderation.ModerationResponse] =
      Moderation.createModeration(input, model)(apiKey)
  }

}
