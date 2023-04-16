package com.raisondata.openai.files

import com.raisondata.openai.SttpStubT
import com.raisondata.openai.api.files.{File, FileMarshaller}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.{Scope, Task}
import zio.test._

object FileSpec extends SttpStubT with FileMarshaller {
  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] = backend
    .whenRequestMatches(
      _.uri.path.startsWith(List("v1", "files"))
    )
    .thenRespond(
      Response(
        Right(FileResponse(None, Some("list"), None, None, None, None, None)),
        StatusCode.Ok
      )
    )

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("files api")(
      test("list files without doing any uploads") {
        for {
          res <- File.listFiles(apiKey)(testingBackend)
        } yield assertTrue(res.filename.isEmpty)
      }
    )

}
