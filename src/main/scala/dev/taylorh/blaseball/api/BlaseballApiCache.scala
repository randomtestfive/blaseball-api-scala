package dev.taylorh.blaseball.api

import dev.taylorh.blaseball.model._
import dev.taylorh.blaseball.model.feed.FeedItem

class BlaseballApiCache(blaseballApi: BlaseballApi) extends BlaseballApi {
  private var leagueCache: Set[League] = Set.empty
  private var subleagueCache: Set[Subleague] = Set.empty
  private var divisionCache: Set[Division] = Set.empty

  private var teamCache: Set[Team] = Set.empty
  private var allTeamsRetrieved: Boolean = false
  private var playerCache: Set[Player] = Set.empty

  override def allTeams: Set[Team] = {
    if(!allTeamsRetrieved) {
      teamCache = blaseballApi.allTeams
      allTeamsRetrieved = true
    }
    teamCache
  }

  override def division(id: String): Division =
    divisionCache.find(_.id == id).getOrElse({
      val real = blaseballApi.division(id)
      divisionCache = divisionCache + real
      real
    })

  override def league(id: String): League =
    leagueCache.find(_.id == id).getOrElse({
      val real = blaseballApi.league(id)
      leagueCache = leagueCache + real
      real
    })

  override def subleague(id: String): Subleague =
    subleagueCache.find(_.id == id).getOrElse({
      val real = blaseballApi.subleague(id)
      subleagueCache = subleagueCache + real
      real
    })

  override def team(id: String): Team =
    teamCache.find(_.id == id).getOrElse({
      val real = blaseballApi.team(id)
      teamCache = teamCache + real
      real
    })

  override def players(ids: Seq[String]): Seq[Player] = {
    val notCached = ids.toSet.diff(playerCache.map(_.id)).toSeq
    playerCache = playerCache ++ blaseballApi.players(notCached)
    ids.map(id => playerCache.find(_.id == id).get)
  }

  override def playerFeed(id: String,
                          limit: Option[Int],
                          sort: Option[Int],
                          category: Option[Int],
                          start: Option[String]): Seq[FeedItem] =
    blaseballApi.playerFeed(id, limit, sort, category, start)
}
