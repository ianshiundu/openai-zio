package com.raisondata

import sttp.capabilities.zio.ZioStreams
import sttp.client4._
import sttp.client4.httpclient.zio._
import sttp.model.StatusCode
import zio._

package object helpers {

  val maxRetries = 3
  val initialDelay: zio.Duration = 1.second
  val maxDelay: zio.Duration = 30.seconds

  def makeRequest[T](request: Request[T])(implicit
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, Response[T]] =
    (for {
      response <- send(request)
      _ <- ZIO.logInfo(s"Making request to OpenAI")
      result <-
        if (response.code.isSuccess) ZIO.succeed(response)
        else if (
          response.code == StatusCode.TooManyRequests && maxRetries > 0
        ) {
          println(
            s" Too many requests were made retrying after 1 minute = ${response.code}"
          )
          val delay = 60.seconds
          ZIO.sleep(delay) *> makeRequest(request).retry(
            Schedule.exponential(initialDelay) && Schedule.recurs(
              maxRetries
            ) && Schedule.spaced(maxDelay)
          )
        } else
          ZIO.fail(
            new RuntimeException(s"Request failed with status ${response.code}")
          )
    } yield result).provide(ZLayer.succeed(backend))
}
