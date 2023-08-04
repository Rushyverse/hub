package com.github.rushyverse.hub.client

import com.github.rushyverse.api.player.Client
import kotlinx.coroutines.CoroutineScope
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ClientHub(
    uuid: UUID, scope:
    CoroutineScope
) : Client(uuid, scope) {

    var canSeePlayers: Boolean = true
        private set

    fun canSeePlayers(canSee: Boolean, plugin: JavaPlugin) {
        canSeePlayers = canSee
        if(canSee) {
            showOtherPlayers(plugin)
        } else {
            hideOtherPlayers(plugin)
        }
    }

    fun hideOtherPlayers(plugin: JavaPlugin) {
        val player = requirePlayer()
        getOtherPlayers(plugin).forEach { player.hidePlayer(plugin, it) }
    }

    fun showOtherPlayers(plugin: JavaPlugin) {
        val player = requirePlayer()
        getOtherPlayers(plugin).forEach { player.showPlayer(plugin, it) }
    }

    private fun getOtherPlayers(plugin: JavaPlugin): Sequence<Player> = Bukkit.getOnlinePlayers().asSequence()
        .filterNotNull()
        .filterNot { it.uniqueId == playerUUID }
}