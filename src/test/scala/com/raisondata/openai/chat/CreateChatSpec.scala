package com.raisondata.openai.chat

import com.raisondata.openai.api.chat.{ChatMarshaller, CreateChat}
import com.raisondata.openai.{Model, Role, SttpStubT}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.Task
import zio.test.{Spec, assertTrue}

object CreateChatSpec extends SttpStubT with ChatMarshaller {

  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] =
    backend
      .whenRequestMatches(
        _.uri.path.startsWith(List("v1", "chat", "completions"))
      )
      .thenRespond(
        Response(
          Right(
            ChatResponse(
              "chatcmpl-74ALqqCtViR9nLkF5qpN1n3jkgt6l",
              "chat.completion",
              1681227458,
              "gpt-3.5-turbo-0301",
              List(
                Choices(
                  0,
                  Message(
                    Role.assistant,
                    "Hello there! How can I assist you today?"
                  ),
                  "stop"
                )
              ),
              Usage(10, 10, 20)
            )
          ),
          StatusCode.Ok
        )
      )

  def spec: Spec[Any, Throwable] = suite("chat mock")(
    test("sending a chat") {
      val sut = CreateChat.createChat(
        Model.gpt_3_5_turbo,
        List(CreateChat.Message(Role.user, "Hello!")),
        Some("end-user-123"),
        1,
        1,
        1,
        stream = false,
        0,
        0,
        Map(),
        None,
        None
      )(apiKey)(testingBackend)

      for {
        res <- sut
      } yield assertTrue(
        res.choices
          .map(_.message.content)
          .mkString == "Hello there! How can I assist you today?"
      )
    }
  )
}
