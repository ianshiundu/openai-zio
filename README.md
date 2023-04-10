# OpenAI Scala Client [![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](https://opensource.org/licenses/MIT)

A simple Scala ZIO wrapper library to use with OpenAI's GPT-3.5+.

## Example

```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Audio
          .transcribe(
            "/path/to/audio/audio.mp3"
          )
    } yield response
  }
}

```

## Usage
To start making calls to OpenAI, you will need to instantiate the `OpenAI` class with your OpenAI API Key i.e.

```
val service = new OpenAI("YOUR_API_KEY")
```

## Supported APIs
1. [Models](https://platform.openai.com/docs/api-reference/models)

To access the Models API, you can access it through `yourOpenAIInstance.Models._`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Models.listModels
    } yield response
  }
}
```
2. [Completions](https://platform.openai.com/docs/api-reference/completions)

To access the Completions API, you can access it through `yourOpenAIInstance.Completions.createCompletion`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai.Model

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Completions.createCompletion(
          model = Model.gpt_3_5_turbo,
          prompt = "Say this is a test",
          user = Some("end-user-id")
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```

3. [Chat](https://platform.openai.com/docs/api-reference/chat)

To access the Chat API (non-streaming), you can access it through `yourOpenAIInstance.Chat.createChat`:

```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai.Model
import com.raisondata.openai.api.chat.CreateChat

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Chat.createChat(
          model = Model.gpt_3_5_turbo,
          messages = List(CreateChat.Message("user", "Hello!")),
          user = Some("end-user-id")
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```
4. [Edits](https://platform.openai.com/docs/api-reference/edits)

To access the Edits API, you can access it through `yourOpenAIInstance.Edits.createEdit`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai.Model

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Edits.createEdit(
          model = Model.text_davinci_edit_001,
          input = "What day of the wek is it?",
          instruction = "Fix the spelling mistakes"
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```
5. [Images](https://platform.openai.com/docs/api-reference/images)

You can access the Images APIs through `yourOpenAIInstance.Images._`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai._

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Images.generateImage(
          prompt = "A cute baby sea otter",
          user = Some("end-user-id"),
          size = `512x512` //Defaults to 1024x1024
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```
6. [Embeddings](https://platform.openai.com/docs/api-reference/embeddings)

You can access the Embeddings API through `yourOpenAIInstance.Embeddings.createEmbeddings`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai.Model

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Embeddings.createEmbeddings(
          model = Model.text_embedding_ada_002,
          input = "The food was delicious and the waiter...",
          user = Some("end-user-id")
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```
7. [Files](https://platform.openai.com/docs/api-reference/files)

You can access the File APIs through `yourOpenAIInstance.Files._`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai.Model

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Files.listFiles
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```
8. [Moderations](https://platform.openai.com/docs/api-reference/moderations)

You can access the Moderation API through `yourOpenAIInstance.Moderations.createModeration`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai.Model

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Moderations.createModeration(
          input = "I want to kill them."
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```

## TODO
- [ ] Fine-tunes API
- [ ] Chat User roles ADT ("system", "user", or "assistant")
- [ ] Validations i.e. Temperature value, Model types e.t.c.
- [ ] Streaming APIs for Text & Chat completion