package com.raisondata.openai.models

import io.circe._
import io.circe.generic.semiauto._

trait ModelMarshaller {
  case class Model(id: String, `object`: String, owned_by: String)
  case class ListModels(data: List[Model])

  object Model {
    implicit val decoder: Decoder[Model] = deriveDecoder[Model]
  }

  object ListModels {
    implicit val decoder: Decoder[ListModels] = deriveDecoder[ListModels]
  }

}
