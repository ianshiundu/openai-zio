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
### [1. Models](https://platform.openai.com/docs/api-reference/models)

You can access the Models API through `yourOpenAIInstance.Models._`:
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
### [2. Completions](https://platform.openai.com/docs/api-reference/completions)

You can access the Completions API through `yourOpenAIInstance.Completions.createCompletion`:
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

### [3. Chat](https://platform.openai.com/docs/api-reference/chat)

You can access the Chat API (non-streaming) through `yourOpenAIInstance.Chat.createChat`:

```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai._
import com.raisondata.openai.api.chat.CreateChat

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.Chat.createChat(
          model = Model.gpt_3_5_turbo,
          messages = List(CreateChat.Message(Role.user, "Hello!")), // Role.user, Role.assistant, Role.system
          user = Some("end-user-id")
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```
### [4. Edits](https://platform.openai.com/docs/api-reference/edits)

You can access the Edits API through `yourOpenAIInstance.Edits.createEdit`:
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
          input = "What day of the wek is it?",
          instruction = "Fix the spelling mistakes"
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```
### [5. Images](https://platform.openai.com/docs/api-reference/images)

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
### [6. Embeddings](https://platform.openai.com/docs/api-reference/embeddings)

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
### [7. Files](https://platform.openai.com/docs/api-reference/files)

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
### [8. Moderations](https://platform.openai.com/docs/api-reference/moderations)

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

### [9. Fine-tunes](https://platform.openai.com/docs/api-reference/fine-tunes)

You can access the Fine-tunes API through `yourOpenAIInstance.Moderations.createModeration`:
```scala
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import com.raisondata.openai.OpenAI
import com.raisondata.openai.Model

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = {

    val service = new OpenAI("YOUR_API_KEY")

    for {
      response <-
        service.FineTunes.createFineTune(
          trainingFile = "id_of_uploaded_file"
        )
      _ <- ZIO.logInfo(s"Got back a response: $response")
    } yield response
  }
}
```

## TODO
- [ ] Validations i.e. Temperature value, Model types e.t.c.
- [ ] Unit tests