package com.raisondata.openai.api.fine_tunes

import io.circe._
import io.circe.generic.semiauto._

trait FineTuneMarshaller {
  case class FineTuneRequest(
      training_file: String,
      model: String,
      n_epochs: Int,
      prompt_loss_weight: Double,
      compute_classification_metrics: Boolean,
      classification_n_classes: Option[Int],
      classification_positive_class: Option[String],
      classification_betas: Option[Array[String]],
      batch_size: Option[Int],
      learning_rate_multiplier: Option[Double],
      validation_file: Option[String],
      suffix: Option[String]
  )

  object FineTuneRequest {
    implicit val encoder: Encoder[FineTuneRequest] =
      deriveEncoder[FineTuneRequest]
  }

  case class Events(
      `object`: String,
      created_at: Long,
      level: String,
      message: String
  )

  case class HyperParams(
      batch_size: Int,
      learning_rate_multiplier: Double,
      n_epochs: Int,
      prompt_loss_weight: Double
  )

  case class FilesBody(
      id: String,
      `object`: String,
      bytes: Long,
      created_at: Long,
      filename: String,
      purpose: String
  )

  case class FineTuneResponse(
      id: String,
      `object`: String,
      model: String,
      created_at: Long,
      events: List[Events],
      fine_tuned_model: Option[String],
      hyperparams: HyperParams,
      organization_id: String,
      result_files: List[FilesBody],
      status: String,
      validation_files: List[FilesBody],
      training_files: List[FilesBody],
      updated_at: Long
  )

  object Events {
    implicit val decoder: Decoder[Events] = deriveDecoder[Events]
  }

  object HyperParams {
    implicit val decoder: Decoder[HyperParams] = deriveDecoder[HyperParams]
  }

  object FilesBody {
    implicit val decoder: Decoder[FilesBody] = deriveDecoder[FilesBody]
  }

  object FineTuneResponse {
    implicit val decoder: Decoder[FineTuneResponse] =
      deriveDecoder[FineTuneResponse]
  }

  case class ListFineTuneResponse(
      `object`: String,
      data: List[FineTuneResponse]
  )

  object ListFineTuneResponse {
    implicit val decoder: Decoder[ListFineTuneResponse] =
      deriveDecoder[ListFineTuneResponse]
  }

  case class ListFineTuneEvents(`object`: String, data: List[Events])

  object ListFineTuneEvents {
    implicit val decoder: Decoder[ListFineTuneEvents] =
      deriveDecoder[ListFineTuneEvents]
  }

  case class DeleteModelResponse(id: String, `object`: String, deleted: Boolean)

  object DeleteModelResponse {
    implicit val decoder: Decoder[DeleteModelResponse] = deriveDecoder[DeleteModelResponse]
  }
}
