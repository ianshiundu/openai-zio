package com.raisondata.openai.api.moderations

import io.circe._
import io.circe.generic.semiauto._

trait ModerationMarshaller {
  case class ModerationRequest(input: String, model: String)

  object ModerationRequest {
    implicit val encoder: Encoder.AsObject[ModerationRequest] =
      deriveEncoder[ModerationRequest]
  }

  case class Categories(
      hate: Boolean,
      `hate/threatening`: Boolean,
      `self-harm`: Boolean,
      sexual: Boolean,
      `sexual/minors`: Boolean,
      violence: Boolean,
      `violence/graphic`: Boolean
  )

  case class CategoryScores(
      hate: Double,
      `hate/threatening`: Double,
      `self-harm`: Double,
      sexual: Double,
      `sexual/minors`: Double,
      violence: Double,
      `violence/graphic`: Double
  )

  case class Result(
      categories: Categories,
      category_scores: CategoryScores,
      flagged: Boolean
  )

  case class ModerationResponse(
      id: String,
      model: String,
      results: List[Result]
  )

  object Result {
    implicit val decoder: Decoder[Result] = deriveDecoder[Result]
  }

  object Categories {
    implicit val decoder: Decoder[Categories] = deriveDecoder[Categories]
  }

  object CategoryScores {
    implicit val decoder: Decoder[CategoryScores] =
      deriveDecoder[CategoryScores]
  }

  object ModerationResponse {
    implicit val decoder: Decoder[ModerationResponse] =
      deriveDecoder[ModerationResponse]
  }
}
