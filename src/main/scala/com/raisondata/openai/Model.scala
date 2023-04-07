package com.raisondata.openai

/** Model Enums for the models compatible with the OpenAI API endpoints
  * @see
  *   <a
  *   href="https://platform.openai.com/docs/models/model-endpoint-compatibility">Model
  *   Endpoint Compatibility</a>
  */
object Model extends Enumeration {
  type Model = Value
  val gpt_4, gpt_3_5_turbo, gpt_3_5_turbo_0301, text_davinci_003,
      text_davinci_002, text_curie_001, text_babbage_001, text_ada_001,
      whisper_1, text_embedding_ada_002, text_search_ada_doc_001,
      text_moderation_stable, text_moderation_latest, davinci, curie, babbage,
      ada, text_davinci_edit_001, code_davinci_edit_001 = Value

  def parse(model: Model): String = {
    model match {
      case `gpt_4`                   => "gpt-4"
      case `gpt_3_5_turbo`           => "gpt-3.5-turbo"
      case `gpt_3_5_turbo_0301`      => "gpt-3.5-turbo-0301"
      case `text_davinci_003`        => "text-davinci-003"
      case `text_davinci_002`        => "text-davinci-002"
      case `text_curie_001`          => "text-curie-001"
      case `text_babbage_001`        => "text-babbage-001"
      case `text_ada_001`            => "text-ada-001"
      case `whisper_1`               => "whisper-1"
      case `text_embedding_ada_002`  => "text-embedding-ada-002"
      case `text_search_ada_doc_001` => "text-search-ada-doc-001"
      case `text_moderation_stable`  => "text-moderation-stable"
      case `text_moderation_latest`  => "text-moderation-latest"
      case `davinci`                 => "davinci"
      case `curie`                   => "curie"
      case `babbage`                 => "babbage"
      case `ada`                     => "ada"
      case `text_davinci_edit_001`   => "text-davinci-edit-001"
      case `code_davinci_edit_001`   => "code-davinci-edit-001"
      case _ => throw new IllegalArgumentException("Model does not exist.")
    }
  }
}
