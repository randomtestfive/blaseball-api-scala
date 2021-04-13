package dev.taylorh.blaseball.api

import dev.taylorh.blaseball.model.feed.FeedItem
import dev.taylorh.blaseball.model.{Division, League, Player, Subleague, Team}
import play.api.libs.json.{Json, Reads}

import scala.io.Source

object BlaseballApiNoCache extends BlaseballApi {
  private implicit val teamSetReads: Reads[Set[Team]] = Reads.set[Team]
  private implicit val playerSeqReads: Reads[Seq[Player]] = Reads.seq[Player]

  private def getUrl(endpoint: String): String = {
    val source = Source.fromURL(apiBase + endpoint)
    val result = source.mkString
    source.close()
    result
  }

  private def get[A](endpoint: String, parameter: String, parameterName: String = "id")(implicit aReads: Reads[A]): A =
    Json.parse(getUrl(endpoint + s"?$parameterName=$parameter")).as[A]

  override def allTeams: Set[Team] =
    Json.parse(getUrl("allTeams")).as[Set[Team]]

  override def team(id: String): Team = get[Team]("team", id)

  override def subleague(id: String): Subleague = get[Subleague]("subleague", id)

  override def division(id: String): Division = get[Division]("division", id)

  override def players(ids: Seq[String]): Seq[Player] = get[Seq[Player]]("players", ids.mkString(","), parameterName = "ids")

  override def league(id: String): League = get[League]("league", id)

  override def playerFeed(id: String,
                          limit: Option[Int],
                          sort: Option[Int],
                          category: Option[Int],
                          start: Option[String]): Seq[FeedItem] = {
    val base = s"feed/player?id=${id}"
    val withLimit = limit.fold(base)(l => base + s"&limit=$l")
    val withSort = sort.fold(withLimit)(s => withLimit + s"&sort=$s")
    val withCategory = category.fold(withSort)(c => withSort + s"&category=$c")
    val withStart = start.fold(withCategory)(s => withCategory + s"&start=$s")
    Json.parse(getUrl(withStart)).as[Seq[FeedItem]]
  }
}
