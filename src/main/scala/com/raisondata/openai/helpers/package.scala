package com.raisondata.openai

import sttp.capabilities.zio.ZioStreams
import sttp.client4._
import sttp.client4.httpclient.zio._
import sttp.model.StatusCode
import zio._

/** Object containing helper functions.
  */
package object helpers {

  private val maxRetries = 3
  private val initialDelay: zio.Duration = 1.second
  private val maxDelay: zio.Duration = 30.seconds

  /** Here, `makeRequest` is a function that takes a request and sends it using
    * sttp's send method. If the response code is a success code, it returns the
    * response. If it's a 429 status code, it use ZIO scheduling with
    * exponential backoff to retry the request with increasing delays between
    * retries.
    *
    * @param request
    *   takes a Request[T]
    *
    * @see
    *   <a
    *   href="https://platform.openai.com/docs/guides/rate-limits/rate-limits">Rate
    *   Limits</a>
    */
  def makeRequest[T](request: Request[T])(implicit
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, Response[T]] =
    (for {
      response <- send(request)
      _ <- ZIO.logInfo(s"Making a request to OpenAI")
      result <-
        if (response.code.isSuccess) ZIO.succeed(response)
        else if (
          response.code == StatusCode.TooManyRequests && maxRetries > 0
        ) {
          ZIO.logInfo(
            "Too many requests were made retrying after 1 minute = ${response.code}"
          )
          val delay = 60.seconds
          ZIO.sleep(delay) *> makeRequest(request).retry(
            Schedule.exponential(initialDelay) && Schedule.recurs(
              maxRetries
            ) && Schedule.spaced(maxDelay)
          )
        } else
          ZIO.fail(
            new RuntimeException(
              s"Request failed with status ${response.code} ${response.body}"
            )
          )
    } yield result).provide(ZLayer.succeed(backend))
}
