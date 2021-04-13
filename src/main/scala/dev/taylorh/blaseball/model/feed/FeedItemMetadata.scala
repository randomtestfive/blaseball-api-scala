package dev.taylorh.blaseball.model.feed

import play.api.libs.json.{JsPath, JsSuccess, JsValue, Json, Reads}
import play.api.libs.functional.syntax._

sealed trait FeedItemMetadata

object FeedItemMetadata {
  case object NoMetadata extends FeedItemMetadata

  case class GainMod(mod: String,
                     play: Option[Int],
                     newModType: Int,
                     parent: Option[String],
                     subPlay: Option[Int]) extends FeedItemMetadata
  private implicit val gainModReads: Reads[GainMod] = (
    (JsPath \ "mod").read[String] and
      (JsPath \ "play").readNullable[Int] and
      (JsPath \ "type").read[Int] and
      (JsPath \ "parent").readNullable[String] and
      (JsPath \ "subPlay").readNullable[Int]
    )(GainMod.apply _)

  case class LoseMod(mod: String,
                     play: Option[Int],
                     newModType: Int,
                     parent: Option[String],
                     subPlay: Option[Int]) extends FeedItemMetadata
  private implicit val loseModReads: Reads[LoseMod] = (
    (JsPath \ "mod").read[String] and
      (JsPath \ "play").readNullable[Int] and
      (JsPath \ "type").read[Int] and
      (JsPath \ "parent").readNullable[String] and
      (JsPath \ "subPlay").readNullable[Int]
    )(LoseMod.apply _)

  case class EchoMod(mod: String, modType: Int)
  private implicit val echoModReads: Reads[EchoMod] = (
    (JsPath \ "mod").read[String] and
      (JsPath \ "type").read[Int]
    )(EchoMod.apply _)

  case class Echo(adds: Seq[EchoMod], play: Int, parent: String, source: String, subPlay: Int) extends FeedItemMetadata
  private implicit val echoReads: Reads[Echo] = Json.reads[Echo]

  implicit val metadataReads: Reads[FeedItemMetadata] = (json: JsValue) => {
    (json \ "type").as[Int] match {
      case 106 => JsSuccess((json \ "metadata").as[GainMod])
      case 107 => JsSuccess((json \ "metadata").as[LoseMod])
      case 172 => JsSuccess((json \ "metadata").as[Echo])
      case _ => JsSuccess(NoMetadata)
    }
  }
}
