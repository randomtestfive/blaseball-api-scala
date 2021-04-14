package dev.taylorh.blaseball.model

import dev.taylorh.blaseball.model.Item.NamePart
import dev.taylorh.blaseball.util.JsonUtil
import play.api.libs.json.{JsPath, JsSuccess, JsValue, Json, Reads}
import play.api.libs.functional.syntax._

case class Item(id: String,
                name: String,
                forger: Option[String],
                forgerName: Option[String],
                prePrefix: Option[NamePart],
                prefixes: Option[Seq[NamePart]],
                postPrefix: Option[NamePart],
                root: Option[NamePart],
                suffix: Option[NamePart],
                durability: Int,
                health: Int,
                baserunningRating: Double,
                pitchingRating: Double,
                hittingRating: Double,
                defenseRating: Double
               ) {
  def allNameParts: Seq[NamePart] = prefixes.getOrElse(Seq.empty) ++ prePrefix ++ postPrefix ++ root ++ suffix
}

object Item {
  private val renameType = JsonUtil.rename("type", "adjustmentType")

  sealed trait ItemAdjustment

  case class StatAdjustment(stat: Int, adjustmentType: Int, value: Double) extends ItemAdjustment
  private implicit val statAdjustmentReads: Reads[StatAdjustment] = renameType andThen Json.reads[StatAdjustment]
  case class ModAdjustment(mod: String, adjustmentType: Int) extends ItemAdjustment
  private implicit val modAdjustmentReads: Reads[ModAdjustment] = renameType andThen Json.reads[ModAdjustment]
  case class UnknownAdjustment(json: JsValue) extends ItemAdjustment

  private implicit val itemAdjustmentReads: Reads[ItemAdjustment] = (json: JsValue) =>
    (json \ "type").as[Int] match {
      case 0 => JsSuccess(json.as[ModAdjustment])
      case 1 => JsSuccess(json.as[StatAdjustment])
      case _ => JsSuccess(UnknownAdjustment(json))
    }

  case class NamePart(name: String, adjustments: Seq[ItemAdjustment])
  private implicit val namePartReads: Reads[NamePart] = Json.reads[NamePart]

  implicit val itemReads:Reads[Item] = Json.reads[Item]
}
