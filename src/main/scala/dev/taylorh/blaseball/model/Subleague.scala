package dev.taylorh.blaseball.model

import dev.taylorh.blaseball.api.BlaseballApi
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class Subleague(id: String,
                     divisionIds: Set[String],
                     name: String
                    ) {
  def getDivisions(implicit blaseballApi: BlaseballApi): Set[Division] =
    divisionIds.map(blaseballApi.division)
}

object Subleague {
  implicit val subleagueReads: Reads[Subleague] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "divisions").read[Set[String]] and
      (JsPath \ "name").read[String]
  )(Subleague.apply _)
}
