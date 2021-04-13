package dev.taylorh.blaseball.model.feed

import dev.taylorh.blaseball.model.feed.FeedItemMetadata._
import play.api.libs.json.{JsPath, JsResult, JsSuccess, JsValue, Json, Reads}
import play.api.libs.functional.syntax._

case class FeedItem(id: String,
                    playerTags: Seq[String],
                    teamTags: Seq[String],
                    gameTags: Seq[String],
                    metadata: FeedItemMetadata,
                    createdString: String,
                    season: Int,
                    tournament: Int,
                    feedItemType: Int,
                    day: Int,
                    phase: Int,
                    category: Int,
                    description: String,
                    nuts: Int
                   ) {

  def addedMods: Seq[String] =
    metadata match {
      case GainMod(mod, _, _, _, _) => Seq(mod)
      case Echo(adds, _, _, _, _) => adds.map(_.mod)
      case _ => Seq.empty
    }
}

object FeedItem {
  implicit val feedItemReads: Reads[FeedItem] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "playerTags").read[Seq[String]] and
      (JsPath \ "teamTags").read[Seq[String]] and
      (JsPath \ "gameTags").read[Seq[String]] and
      JsPath.read[FeedItemMetadata] and
      (JsPath \ "created").read[String] and
      (JsPath \ "season").read[Int] and
      (JsPath \ "tournament").read[Int] and
      (JsPath \ "type").read[Int] and
      (JsPath \ "day").read[Int] and
      (JsPath \ "phase").read[Int] and
      (JsPath \ "category").read[Int] and
      (JsPath \ "description").read[String] and
      (JsPath \ "nuts").read[Int]
  )(FeedItem.apply _)
}
