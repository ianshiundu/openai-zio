package com.raisondata.openai.api.fine_tunes

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
}
