package com.raisondata.openai.moderations

import com.raisondata.openai.api.moderations.{Moderation, ModerationMarshaller}
import com.raisondata.openai.{Model, SttpStubT}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.test._
import zio.{Scope, Task}

object ModerationSpec extends SttpStubT with ModerationMarshaller {

  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] = backend
    .whenRequestMatches(
      _.uri.path.startsWith(List("v1", "moderations"))
    )
    .thenRespond(
      Response(
        Right(
          ModerationResponse(
            "modr-75zl7qtL47ssZqr9BDe08o3vNSCBj",
            "text-moderation-004",
            List(
              Result(
                Categories(false, false, false, false, false, true, false),
                CategoryScores(0.180674210190773, 0.003288434585556388,
                  1.8088556208439854e-9, 9.759669410414062e-7,
                  1.3363569806301712e-8, 0.8864424824714661,
                  3.2010063932830235e-8),
                flagged = true
              )
            )
          )
        ),
        StatusCode.Ok
      )
    )

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("moderation api")(
      test("flags an input that violets OpenAI's content policy") {
        for {
          res <- Moderation.createModeration(
            "I want to kill them.",
            Model.text_moderation_latest
          )(apiKey)(testingBackend)
        } yield assertTrue(res.results.exists(_.flagged))
      }
    )
}
