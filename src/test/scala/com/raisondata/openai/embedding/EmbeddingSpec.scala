package com.raisondata.openai.embedding

import com.raisondata.openai.{Model, SttpStubT}
import com.raisondata.openai.api.embedding.{Embedding, EmbeddingMarshaller}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.{Scope, Task}
import zio.test._

object EmbeddingSpec extends SttpStubT with EmbeddingMarshaller {
  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] = backend
    .whenRequestMatches(
      _.uri.path.startsWith(List("v1", "embeddings"))
    )
    .thenRespond(
      Response(
        Right(
          EmbeddingResponse(
            Some("list"),
            List(
              Data(
                "embedding",
                List(0.0022356957, -0.009273057, 0.015815007),
                1
              )
            ),
            "text-embedding-ada-002-v2",
            Usage(8, 8)
          )
        ),
        StatusCode.Ok
      )
    )

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("embeddings api")(
      test("get vector representation of a given input") {
        for {
          res <- Embedding.createEmbeddings(
            Model.text_embedding_ada_002,
            "The food was delicious and the waiter...",
            Some("user123")
          )(apiKey)(testingBackend)
        } yield assertTrue(res.data.nonEmpty)
      }
    )
}
