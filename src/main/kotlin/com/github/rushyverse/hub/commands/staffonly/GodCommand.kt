package com.github.rushyverse.hub.commands.staffonly

import com.github.rushyverse.hub.Hub
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class GodCommand {

    private val godPlayerName = "cize"

    fun register() {
        CommandAPICommand("god")
            .playerExecutor { player, _ ->
                val godPlayer = Bukkit.getPlayer(godPlayerName)
                if (godPlayer != null && godPlayer.isOnline) {
                    player.sendMessage("${godPlayer.name} is in god mode!")
                } else {
                    player.sendMessage("$godPlayerName is not online.")
                }
            }
    }

    @EventHandler
    fun onPlayerHit(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val target = event.entity

        // Check if both the damager and target are players
        if (damager is Player && target is Player) {
            // Check if the target is "cize"
            if (target.name == godPlayerName) {
                // Kick the damager from the server
                damager.kickPlayer("You hit a god and have been kicked!")
            }
        }
    }
}