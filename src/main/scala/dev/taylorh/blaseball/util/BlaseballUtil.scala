package dev.taylorh.blaseball.util

import dev.taylorh.blaseball.api.BlaseballApi
import dev.taylorh.blaseball.model.{Player, Team}

object BlaseballUtil {
  def getTeamsInLeague(id: String)(implicit blaseballApi: BlaseballApi): Set[Team] = {
    blaseballApi
      .league(id)
      .getSubleagues
      .flatMap(_.getDivisions)
      .flatMap(_.getTeams)
  }

  def getPlayersInLeague(id: String)(implicit blaseballApi: BlaseballApi): Set[Player] = {
    val ids = getTeamsInLeague(id)
      .flatMap(t => t.lineupIds ++ t.rotationIds ++ t.bullpenIds ++ t.benchIds)
    blaseballApi.players(ids.toSeq).toSet
  }

  def getNonShadowPlayersInLeague(id: String)(implicit blaseballApi: BlaseballApi): Set[Player] = {
    val ids = getTeamsInLeague(id)
      .flatMap(t => t.lineupIds ++ t.rotationIds)
    blaseballApi.players(ids.toSeq).toSet
  }
}
