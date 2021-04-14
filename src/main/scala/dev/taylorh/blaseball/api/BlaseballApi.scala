package dev.taylorh.blaseball.api

import dev.taylorh.blaseball.model.feed.FeedItem
import dev.taylorh.blaseball.model.{Division, League, Player, Subleague, Team}

trait BlaseballApi {
  //def allDivisions: Set[Division]

  def allTeams: Set[Team]

  def division(id: String): Division

  //def gameByDate(season: Int, day: Int): GameUpdate
  //def game(id: String): GameUpdate

  //def gameStatsheet(id: String): GameStatsheet

  def league(id: String): League

  def subleague(id: String): Subleague

  //TODO needs update
  def team(id: String): Team

  //TODO actually do
  def players(ids: Seq[String]): Seq[Player]

  def playerFeed(id: String,
                 limit: Option[Int] = None,
                 sort: Option[Int] = None,
                 category: Option[Int] = None,
                 start: Option[String] = None): Seq[FeedItem]
}
