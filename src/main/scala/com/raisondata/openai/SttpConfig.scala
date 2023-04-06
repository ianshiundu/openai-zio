package com.raisondata.openai

import com.raisondata.openai.helpers.makeRequest
import sttp.capabilities.zio.ZioStreams
import sttp.model.MediaType.MultipartFormData
import sttp.model.Part

trait SttpConfig {

  import sttp.client4._
  import sttp.model.MediaType.ApplicationJson
  import zio._

  def usage: String
  def domain: String

  private val baseURL = "https://api.openai.com"

  private val version = "v1"

  private val readTimeout = 5.minute.asScala

  val uri = uri"$baseURL/$version/$domain/$usage"

  def requestWithJsonBody[B: BodySerializer, T](
      body: B,
      response: ResponseAs[T]
  )(openaiAPIKey: String): Request[T] =
    basicRequest
      .post(uri)
      .header("Authorization", s"Bearer $openaiAPIKey")
      .contentType(ApplicationJson)
      .body(body)
      .readTimeout(readTimeout)
      .response(response)

  def requestWithMultipartForm[T](
      ps: Seq[Part[BasicBodyPart]],
      response: ResponseAs[T]
  )(openaiAPIKey: String): Request[T] = basicRequest
    .post(uri)
    .header("Authorization", s"Bearer $openaiAPIKey")
    .contentType(MultipartFormData)
    .multipartBody(ps)
    .readTimeout(readTimeout)
    .response(response)

  def getResponse[T](request: Request[T])(
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, Response[T]] =
    makeRequest(request)(backend)

}
