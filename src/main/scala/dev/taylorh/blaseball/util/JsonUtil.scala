package dev.taylorh.blaseball.util

import play.api.libs.json.{JsObject, Reads, __}

object JsonUtil {
  def rename(from: String, to: String): Reads[JsObject] = {
    __.json.update(
      (__ \ to).json.copyFrom((__ \ from).json.pick)
    ) andThen
      (__ \ from).json.prune
  }
}
