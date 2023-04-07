package com.raisondata.openai.api.models

import com.raisondata.openai.SttpConfig
import com.raisondata.openai.helpers.makeRequest
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import zio._

object OpenAIModels extends SttpConfig with ModelMarshaller {
  override def domain: String = "models"

  override def usage: String = ""

  override val uri = uri"https://api.openai.com/v1/$domain"

  def listModels(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, OpenAIModels.ListModels] =
    HttpClientZioBackend().flatMap { backend =>
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
  )(openaiAPIKey: String): ZIO[Any, Throwable, OpenAIModels.Model] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .get(uri"$uri/$model")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[Model])

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
}
