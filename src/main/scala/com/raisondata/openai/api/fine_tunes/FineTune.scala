package com.raisondata.openai.api.fine_tunes

import com.raisondata.openai.Model.Model
import com.raisondata.openai.helpers.makeRequest
import com.raisondata.openai.{Model, SttpConfig}
import sttp.client4._
import sttp.client4.circe._
import sttp.client4.httpclient.zio._
import sttp.model.Uri
import zio._

object FineTune extends SttpConfig with FineTuneMarshaller {
  override def domain: String = "fine-tunes"

  override def usage: String = ""

  override val uri: Uri = uri"$baseURL/$version/$domain"

  def createFineTune(
      trainingFile: String,
      model: Model,
      nEpochs: Int,
      promptLossWeight: Double,
      computeClassificationMetrics: Boolean,
      classificationNClasses: Option[Int],
      classificationPositiveClass: Option[String],
      classificationBetas: Option[Array[String]],
      batchSize: Option[Int],
      learningRateMultiplier: Option[Double],
      validationFile: Option[String],
      suffix: Option[String]
  )(openaiAPIKey: String): ZIO[Any, Throwable, FineTune.FineTuneResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val requestBody = FineTuneRequest(
        trainingFile,
        Model.parse(model),
        nEpochs,
        promptLossWeight,
        computeClassificationMetrics,
        classificationNClasses,
        classificationPositiveClass,
        classificationBetas,
        batchSize,
        learningRateMultiplier,
        validationFile,
        suffix
      )

      val request =
        requestWithJsonBody(requestBody, asJson[FineTuneResponse])(openaiAPIKey)

      val response = getResponse(request)(backend)

      response.flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Fine tuning job was completed successfully!")
          } yield response
      })
    }

  def listFineTunes(
      openaiAPIKey: String
  ): ZIO[Any, Throwable, FineTune.ListFineTuneResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .get(uri)
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[ListFineTuneResponse])

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Fine tuning jobs were returned successfully!")
          } yield response
      })
    }

  def retrieveFineTuneJob(
      fineTuneId: String
  )(openaiAPIKey: String): ZIO[Any, Throwable, FineTune.FineTuneResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .get(uri"$uri/$fineTuneId")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[FineTuneResponse])

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Fine tuning job was returned successfully!")
          } yield response
      })
    }

  def cancelFineTuneJob(
      fineTuneId: String
  )(openaiAPIKey: String): ZIO[Any, Throwable, FineTune.FineTuneResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .get(uri"$uri/$fineTuneId/cancel")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[FineTuneResponse])

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Fine tuning job was cancelled successfully!")
          } yield response
      })
    }

  def listFineTuneEvents(
      fineTuneId: String
  )(openaiAPIKey: String): ZIO[Any, Throwable, FineTune.ListFineTuneEvents] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .get(uri"$uri/$fineTuneId/cancel")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[ListFineTuneEvents])

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Fine tune events were returned successfully!")
          } yield response
      })
    }

  def deleteFineTuneModel(model: String)(openaiAPIKey: String): ZIO[Any, Throwable, FineTune.DeleteModelResponse] =
    HttpClientZioBackend().flatMap { backend =>
      val request = basicRequest
        .delete(uri"$uri/models/$model")
        .header("Authorization", s"Bearer $openaiAPIKey")
        .readTimeout(5.minute.asScala)
        .response(asJson[DeleteModelResponse])

      makeRequest(request)(backend).flatMap(_.body match {
        case Left(error) =>
          for {
            _ <- ZIO.logError(
              s"An error occurred while making a request $error"
            )
          } yield throw new RuntimeException(error)
        case Right(response) =>
          for {
            _ <- ZIO.logInfo("Fine tuning job was cancelled successfully!")
          } yield response
      })
    }
}
