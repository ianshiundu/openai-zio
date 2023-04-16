package com.raisondata.openai

import sttp.capabilities.zio.ZioStreams
import sttp.client4.httpclient.zio.HttpClientZioBackend
import sttp.client4.testing.WebSocketStreamBackendStub
import zio.Task
import zio.test.ZIOSpecDefault

import java.util.UUID

trait SttpStubT extends ZIOSpecDefault {
  val apiKey = s"sk-${UUID.randomUUID()}"

  val backend: WebSocketStreamBackendStub[Task, ZioStreams] =
    HttpClientZioBackend.stub

}
