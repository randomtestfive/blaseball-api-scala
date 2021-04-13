package dev.taylorh.blaseball.model

import Player._
import dev.taylorh.blaseball.api.BlaseballApi
import dev.taylorh.blaseball.model.feed.FeedItem
import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Player(id: String,
                  name: String,
                  attributes: PlayerAttributes,
                  baserunning: BaserunningAttributes,
                  defense: DefenseAttributes,
                  hitting: HittingAttributes,
                  pitching: PitchingAttributes,
                  other: OtherAttributes,
                  teamAttributes: TeamAttributes,
                  ratings: Ratings
                 ) {
  def getLeagueTeam(implicit blaseballApi: BlaseballApi): Team = blaseballApi.team(teamAttributes.leagueTeamId)

  def getFeed(limit: Option[Int] = None,
              sort: Option[Int] = None,
              category: Option[Int] = None,
              start: Option[String] = None)
             (implicit blaseballApi: BlaseballApi): Seq[FeedItem] =
    blaseballApi.playerFeed(id, limit, sort, category, start)
}

object Player {
  case class BaserunningAttributes(baseThirst: Double,
                                   continuation: Double,
                                   groundFriction: Double,
                                   indulgence: Double,
                                   laserlikeness: Double
                                  )
  private implicit val baserunningAttributesReads: Reads[BaserunningAttributes] = Json.reads[BaserunningAttributes]

  case class DefenseAttributes(anticapitalism: Double,
                               chasiness: Double,
                               omniscience: Double,
                               tenaciousness: Double,
                               watchfulness: Double
                              )
  private implicit val defenseAttributesReads: Reads[DefenseAttributes] = Json.reads[DefenseAttributes]

  case class HittingAttributes(buoyancy: Double,
                               divinity: Double,
                               martyrdom: Double,
                               moxie: Double,
                               musclitude: Double,
                               patheticism: Double,
                               thwackability: Double,
                               tragicness: Double
                              )
  private implicit val hittingAttributesReads: Reads[HittingAttributes] = Json.reads[HittingAttributes]

  case class PitchingAttributes(coldness: Double,
                                overpowerment: Double,
                                ruthlessness: Double,
                                shakespearianism: Double,
                                suppression: Double,
                                unthwackability: Double,
                                totalFingers: Int
                               )
  private implicit val pitchingAttributesReads: Reads[PitchingAttributes] = Json.reads[PitchingAttributes]

  case class OtherAttributes(deceased: Boolean,
                             peanutAllergy: Boolean,
                             cinnamon: Double,
                             pressurization: Double,
                             soul: Int,
                             ritual: Option[String],
                             coffee: Option[Int],
                             blood: Option[Int],
                             fate: Int,
                             eDensity: Double,
                             evolution: Int
                            )
  private implicit val otherAttributesReads: Reads[OtherAttributes] = Json.reads[OtherAttributes]

  case class TeamAttributes(leagueTeamId: String,
                            tournamentTeamId: Option[String]
                           )
  private implicit val teamAttributesReads: Reads[TeamAttributes] = Json.reads[TeamAttributes]

  case class Ratings(baserunningRating: Double,
                     hittingRating: Double,
                     defenseRating: Double,
                     pitchingRating: Double,
                     hitStreak: Int,
                     consecutiveHits: Int
                    )
  private implicit val ratingsReads: Reads[Ratings] = Json.reads[Ratings]

  case class PlayerAttributes(permanent: Set[String],
                              season: Set[String],
                              week: Set[String],
                              game: Set[String]
                             )
  private implicit val playerAttributesReads: Reads[PlayerAttributes] = (
    (JsPath \ "permAttr").read[Set[String]] and
      (JsPath \ "seasAttr").read[Set[String]] and
      (JsPath \ "weekAttr").read[Set[String]] and
      (JsPath \ "gameAttr").read[Set[String]]
  )(PlayerAttributes.apply _)

  implicit val playerReads: Reads[Player] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "name").read[String] and
      JsPath.read[PlayerAttributes] and
      JsPath.read[BaserunningAttributes] and
      JsPath.read[DefenseAttributes] and
      JsPath.read[HittingAttributes] and
      JsPath.read[PitchingAttributes] and
      JsPath.read[OtherAttributes] and
      JsPath.read[TeamAttributes] and
      JsPath.read[Ratings]
    )(Player.apply _)
}
