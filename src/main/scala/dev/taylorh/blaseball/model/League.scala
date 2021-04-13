package dev.taylorh.blaseball.model

import dev.taylorh.blaseball.api.BlaseballApi
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class League(id: String,
                  subleagueIds: Set[String],
                  name: String,
                  tiebreakersId: String
                 ) {
  def getSubleagues(implicit blaseballApi: BlaseballApi): Set[Subleague] = subleagueIds.map(blaseballApi.subleague)
}

object League {
  implicit val leagueReads: Reads[League] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "subleagues").read[Set[String]] and
      (JsPath \ "name").read[String] and
      (JsPath \ "tiebreakers").read[String]
  )(League.apply _)
}
