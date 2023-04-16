package com.raisondata.openai.api.models

import com.raisondata.openai.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import sttp.capabilities.zio.ZioStreams
import sttp.client4._
import sttp.client4.circe._
import zio._

object OpenAIModels extends SttpConfig with ModelMarshaller {
  override def domain: String = "models"

  override def usage: String = ""

  override val uri = uri"$baseURL/$version/$domain"

  def listModels(
      openaiAPIKey: String
  )(implicit
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, OpenAIModels.ListModels] = {
    val request = basicRequest
      .get(uri)
      .header("Authorization", s"Bearer $openaiAPIKey")
      .readTimeout(5.minute.asScala)
      .response(asJson[ListModels])

    makeRequest(request)(backend)
      .map(_.body match {
        case Left(error) =>
          println(s"An error occurred while making a request $error")
          throw new RuntimeException(error)
        case Right(value) =>
          println(s"Models were returned successfully!")
          println(value)
          value
      })
  }

  def getModel(
      model: String
  )(openaiAPIKey: String)(implicit
      backend: WebSocketStreamBackend[Task, ZioStreams]
  ): ZIO[Any, Throwable, OpenAIModels.Model] = {
    val request = basicRequest
      .get(uri"$uri/$model")
      .header("Authorization", s"Bearer $openaiAPIKey")
      .readTimeout(5.minute.asScala)
      .response(asJson[Model])

    makeRequest(request)(backend).flatMap(_.body match {
      case Left(error) =>
        for {
          _ <- ZIO.logError(
            s"An error occurred while making a request $error"
          )
        } yield throw new RuntimeException(error)
      case Right(response) =>
        for {
          _ <- ZIO.logInfo("Models were returned successfully!")
        } yield response
    })
  }
}
