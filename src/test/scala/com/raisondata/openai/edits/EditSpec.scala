package com.raisondata.openai.edits

import com.raisondata.openai.{Model, SttpStubT}
import com.raisondata.openai.api.edits.{Edit, EditMarshaller}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.{Scope, Task}
import zio.test._

object EditSpec extends SttpStubT with EditMarshaller {
  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] = backend
    .whenRequestMatches(
      _.uri.path.startsWith(List("v1", "edits"))
    )
    .thenRespond(
      Response(
        Right(
          EditResponse(
            "edit",
            1681655933,
            List(
              Choices(
                "What day of the week is it ? Is today Monday ?",
                0
              )
            ),
            Usage(25, 34, 59)
          )
        ),
        StatusCode.Ok
      )
    )

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("edits api")(
      test("edit input text") {

        for {
          res <- Edit.createEdit(
            Model.text_davinci_edit_001,
            "What day of the wek is it?",
            "Fix the spelling mistakes",
            1,
            1,
            1
          )(apiKey)(testingBackend)
        } yield assertTrue(res.choices.nonEmpty)
      }
    )

}
