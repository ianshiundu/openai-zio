package com.raisondata.openai.images

import com.raisondata.openai.{ResponseFormat, SttpStubT, `1024x1024`}
import com.raisondata.openai.api.images.{GenerateImage, ImageMarshaller}
import sttp.capabilities.zio.ZioStreams
import sttp.client4.Response
import sttp.client4.testing.WebSocketStreamBackendStub
import sttp.model.StatusCode
import zio.{Scope, Task}
import zio.test._

object GenerateImageSpec extends SttpStubT with ImageMarshaller {
  val url =
    "https://oaidalleapiprodscus.blob.core.windows.net/private/org-yJQOHBlxUBjE6TRTmb5vWIHp/user-djbUS39FX9lE1mBA8d0F8nkG/img-2vjlEQTGbFJQd2kMrcIqqtlh.png?st=2023-04-16T14%3A53%3A08Z&se=2023-04-16T16%3A53%3A08Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-04-16T15%3A31%3A58Z&ske=2023-04-17T15%3A31%3A58Z&sks=b&skv=2021-08-06&sig=WaJUCy5XiQzozhDNmY3BbatzLhFPmqMUdwSUZbkJiHU%3D"

  val testingBackend: WebSocketStreamBackendStub[Task, ZioStreams] = backend
    .whenRequestMatches(
      _.uri.path.startsWith(List("v1", "images", "generations"))
    )
    .thenRespond(
      Response(
        Right(
          ImageResponse(
            1681660103,
            List(
              DataBody(url)
            )
          )
        ),
        StatusCode.Ok
      )
    )

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("image api")(
      test("generate an image based on a given prompt") {
        for {
          res <- GenerateImage.generateImage(
            "A Corgi as Tommy Vercetti on GTA Vice City",
            `1024x1024`,
            ResponseFormat.url,
            Some("user123"),
            Some(1)
          )(apiKey)(testingBackend)
        } yield assertTrue(res.data.map(_.url).mkString == url)
      }
    )
}
