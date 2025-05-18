package com.github.rushyverse.hub.commands.staffonly

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.hub.Hub
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class WarzoneCommand {

    fun register(plugin: Hub) {

        commandAPICommand("warzone") {
            playerExecutor { player, _ ->

                val players: List<Player> = Bukkit.getOnlinePlayers().filterNotNull()
                val warzoneDuration = 10 * 60 // 10 minutes

                for (player in players) {
                    player.gameMode = GameMode.SURVIVAL
                    player.inventory.addItem(ItemStack(Material.DIAMOND_PICKAXE))
                }

                Bukkit.broadcastMessage("${ChatColor.RED}WARZONE!!!")

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
                    // Restore players to their original game mode
                    for (player in players) {
                        player.gameMode = GameMode.ADVENTURE
                    }
                }, (warzoneDuration * 20).toLong()) // converting seconds to ticks
            }
        }
    }
}