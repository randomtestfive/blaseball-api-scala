package dev.taylorh.blaseball.model

import dev.taylorh.blaseball.api.BlaseballApi
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class Division(id: String,
                    teamIds: Set[String],
                    name: String
                   ) {

  def getTeams(implicit blaseballApi: BlaseballApi): Set[Team] = {
    teamIds.map(blaseballApi.team)
  }
}

object Division {
  implicit val divisionReads: Reads[Division] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "teams").read[Set[String]] and
      (JsPath \ "name").read[String]
  )(Division.apply _)
}
