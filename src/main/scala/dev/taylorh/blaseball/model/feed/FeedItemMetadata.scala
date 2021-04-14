package dev.taylorh.blaseball.model.feed

import dev.taylorh.blaseball.util.JsonUtil
import play.api.libs.json.{JsPath, JsSuccess, JsValue, Json, Reads, __}
import play.api.libs.functional.syntax._

sealed trait FeedItemMetadata

object FeedItemMetadata {
  case object NoMetadata extends FeedItemMetadata

  private val renameType = JsonUtil.rename("type", "metadataType")

  case class GainMod(mod: String,
                     play: Option[Int],
                     metadataType: Int,
                     parent: Option[String],
                     subPlay: Option[Int]) extends FeedItemMetadata
  private implicit val gainModReads: Reads[GainMod] = renameType andThen Json.reads[GainMod]

  case class LoseMod(mod: String,
                     play: Option[Int],
                     metadataType: Int,
                     parent: Option[String],
                     subPlay: Option[Int]) extends FeedItemMetadata
  private implicit val loseModReads: Reads[LoseMod] = renameType andThen Json.reads[LoseMod]

  case class EchoMod(mod: String, metadataType: Int)
  private implicit val echoModReads: Reads[EchoMod] = renameType andThen Json.reads[EchoMod]

  case class Echo(adds: Seq[EchoMod], play: Int, parent: String, source: String, subPlay: Int) extends FeedItemMetadata
  private implicit val echoReads: Reads[Echo] = Json.reads[Echo]

  case class Partying(play: Int, metadataType: Int, after: Double, before: Double, parent: String, subPlay: Int) extends FeedItemMetadata
  private implicit val partyingReads: Reads[Partying] = renameType andThen Json.reads[Partying]

  implicit val metadataReads: Reads[FeedItemMetadata] = (json: JsValue) => {
    (json \ "type").as[Int] match {
      case 106 => JsSuccess((json \ "metadata").as[GainMod])
      case 107 => JsSuccess((json \ "metadata").as[LoseMod])
      case 117 => JsSuccess((json \ "metadata").as[Partying])
      case 172 => JsSuccess((json \ "metadata").as[Echo])
      case _ => JsSuccess(NoMetadata)
    }
  }
}
