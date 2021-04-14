package dev.taylorh.blaseball.api
import dev.taylorh.blaseball.model.{Division, League, Player, Subleague, Team}
import dev.taylorh.blaseball.model.feed.FeedItem
import play.api.libs.json.{JsArray, JsValue, Json, Reads}

import scala.io.Source

object ChroniclerApiNoCache extends BlaseballApi {
  private val apiBase: String = "https://api.sibr.dev/chronicler/v2/entities"

  private def getUrl(endpoint: String): String = {
    val source = Source.fromURL(apiBase + endpoint)
    val result = source.mkString
    source.close()
    result
  }

  private def parse[A](input: String)(implicit aReads: Reads[A]): Seq[A] =
    (Json.parse(input) \ "items")
      .as[JsArray]
      .value
      .map(j => (j \ "data").as[A])
      .toSeq

  private def get[A](endpoint: String, parameter: String, parameterName: String = "id")(implicit aReads: Reads[A]): Seq[A] =
    parse(getUrl( s"?type=$endpoint&$parameterName=$parameter"))


  override def allTeams: Set[Team] = parse[Team](getUrl(apiBase + "?type=team")).toSet

  override def division(id: String): Division = get[Division]("division", id).head

  override def league(id: String): League = get[League]("league", id).head

  override def subleague(id: String): Subleague = get[Subleague]("subleague", id).head

  override def team(id: String): Team = get[Team]("team", id).head

  override def players(ids: Seq[String]): Seq[Player] =
    ids.grouped(50).toSeq.flatMap(p => get[Player]("player", p.mkString(",")))

  override def playerFeed(id: String, limit: Option[Int], sort: Option[Int], category: Option[Int], start: Option[String]): Seq[FeedItem] =
    Seq.empty
}
