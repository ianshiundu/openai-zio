package com.raisondata.openai.audio

import com.raisondata.openai.api.audio.{AudioMarshaller, Transcription}
import com.raisondata.openai.{Language, Model, ResponseFormat, SttpStubT}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.test._
import zio.{Scope, Task}

object TranscribeSpec extends SttpStubT with AudioMarshaller{
  val text: String =
    """Hi Chelsea, I hope this letter finds you well. 
      |That sounds a little ridiculous to say under the circumstances. 
      |You've endured so much and come out such a shining symbol of all that is good in the world. 
      |I really mean that and I think of you often. I sent you a card a few weeks ago, 
      |but I didn't want to complicate the sentiment of the card with the subject I'd like to talk about now. 
      |Allow me to introduce myself.""".stripMargin

  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] = backend
    .whenRequestMatches(
      _.uri.path.startsWith(List("v1", "audio", "transcriptions"))
    )
    .thenRespond(Response(Right(TextResponse(text)), StatusCode.Ok))

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("transcription api")(
      test("transcribe audio") {
        val sut = Transcription.transcribe(
          "audio.mp3",
          Model.whisper_1,
          ResponseFormat.json,
          0,
          Language.en
        )(apiKey)(testingBackend)

        for {
          res <- sut
        } yield assertTrue(res.text == text)
      }
    )
}
