package com.github.rushyverse.hub.tab

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

class ListPLayers : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val team = getOrCreateTeam(scoreboard, "YourTeamName")

        team.addEntry(player.name)

        val headerLines = listOf("${ChatColor.YELLOW}rushy.space ${ChatColor.WHITE}| ${ChatColor.RED}1.8 - 1.20",
                "${ChatColor.GRAY}To report a player use ${ChatColor.RED}/report", "")
        val footerLines = listOf("", "${ChatColor.GREEN}Shop, ${ChatColor.RED}rankings ${ChatColor.YELLOW}and ${ChatColor.LIGHT_PURPLE}more on ${ChatColor.YELLOW}www.rushy.space",
                "${ChatColor.BLUE}Discord: ${ChatColor.WHITE}www.rushy.space/discord")

        player.setPlayerListHeaderFooter(headerLines.joinToString("\n"), footerLines.joinToString("\n"))
    }

    private fun getOrCreateTeam(scoreboard: Scoreboard, name: String): Team {
        return scoreboard.getTeam(name) ?: scoreboard.registerNewTeam(name)
    }
}