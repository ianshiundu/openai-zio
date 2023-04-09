# OpenAI Scala Client (WIP 🚧) [![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](https://opensource.org/licenses/MIT)

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

## TODO
- [x] Images
- [x] Models
- [x] Chat
- [x] Edits
- [x] Embeddings
- [x] Files
- [ ] Fine-tunes
- [x] Moderations