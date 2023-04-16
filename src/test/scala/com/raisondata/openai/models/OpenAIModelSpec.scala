package com.raisondata.openai.models

import com.raisondata.openai.SttpStubT
import com.raisondata.openai.api.models.{ModelMarshaller, OpenAIModels}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.{Scope, Task}
import zio.test._

object OpenAIModelSpec extends SttpStubT with ModelMarshaller {
  val listOfModels: OpenAIModels.ListModels = OpenAIModels.ListModels(
    List(
      OpenAIModels.Model("babbage", "model", "openai"),
      OpenAIModels.Model("davinci", "model", "openai"),
      OpenAIModels.Model("text-davinci-edit-001", "model", "openai"),
      OpenAIModels.Model("babbage-code-search-code", "model", "openai-dev"),
      OpenAIModels.Model("text-similarity-babbage-001", "model", "openai-dev")
    )
  )

  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] = backend
    .whenRequestMatches(
      _.uri.path.startsWith(List("v1", "models"))
    )
    .thenRespond(Response(Right(listOfModels), StatusCode.Ok))

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("model api")(
      test("lists the currently available models") {
        for {
          res <- OpenAIModels.listModels(apiKey)(testingBackend)
        } yield assertTrue(res == listOfModels)
      }
    )

}
