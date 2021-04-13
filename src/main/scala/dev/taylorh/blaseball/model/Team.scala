package dev.taylorh.blaseball.model

import dev.taylorh.blaseball.api.BlaseballApi
import dev.taylorh.blaseball.model.Team._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}

case class Team(id: String,
                lineupIds: Seq[String],
                rotationIds: Seq[String],
                bullpenIds: Seq[String],
                benchIds: Seq[String],
                teamInfo: TeamInfo,
                shameStats: ShameStats,
                championships: Int,
                rotationSlot: Int,
                attributes: TeamAttributes
               ) {
  def getLineupPlayers(implicit blaseballApi: BlaseballApi): Seq[Player] = blaseballApi.players(lineupIds)
  def getRotationPlayers(implicit blaseballApi: BlaseballApi): Seq[Player] = blaseballApi.players(rotationIds)
  def getBullpenPlayers(implicit blaseballApi: BlaseballApi): Seq[Player] = blaseballApi.players(bullpenIds)
  def getBenchPlayers(implicit blaseballApi: BlaseballApi): Seq[Player] = blaseballApi.players(benchIds)
}

object Team {
  case class TeamInfo(fullName: String,
                      location: String,
                      mainColorString: String,
                      nickname: String,
                      secondaryColorString: String,
                      shorthand: String,
                      emojiCode: String,
                      slogan: String
                     )

  private implicit val teamInfoReads: Reads[TeamInfo] = (
    (JsPath \ "fullName").read[String] and
      (JsPath \ "location").read[String] and
      (JsPath \ "mainColor").read[String] and
      (JsPath \ "nickname").read[String] and
      (JsPath \ "secondaryColor").read[String] and
      (JsPath \ "shorthand").read[String] and
      (JsPath \ "emoji").read[String] and
      (JsPath \ "slogan").read[String]
  )(TeamInfo.apply _)

  case class ShameStats(shameRuns: Double,
                        totalShames: Int,
                        totalShamings: Int,
                        seasonShames: Int,
                        seasonShamings: Int
                       )

  private implicit val shameStatsReads: Reads[ShameStats] = Json.reads[ShameStats]

  case class TeamAttributes(week: Set[String],
                            game: Set[String],
                            season: Set[String],
                            permanent: Set[String]
                           )

  private implicit val teamAttributesReads: Reads[TeamAttributes] = (
    (JsPath \ "weekAttr").read[Set[String]] and
      (JsPath \ "gameAttr").read[Set[String]] and
      (JsPath \ "seasAttr").read[Set[String]] and
      (JsPath \ "permAttr").read[Set[String]]
  )(TeamAttributes.apply _)

  implicit val teamReads: Reads[Team] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "lineup").read[Seq[String]] and
      (JsPath \ "rotation").read[Seq[String]] and
      (JsPath \ "bullpen").read[Seq[String]] and
      (JsPath \ "bench").read[Seq[String]] and
      JsPath.read[TeamInfo] and
      JsPath.read[ShameStats] and
      (JsPath \ "championships").read[Int] and
      (JsPath \ "rotationSlot").read[Int] and
      JsPath.read[TeamAttributes]
  )(Team.apply _)
}
