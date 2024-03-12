package com.github.rushyverse.hub.commands

import com.github.rushyverse.hub.Hub
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.hub.client.ClientHub

class HubCommand(
    private val plugin: Hub,
) {
    private val clients: ClientManager by inject(plugin.id)

    fun register() {
        commandAPICommand("hub") {
            withAliases("lobby", "l", "spawn")
            playerExecutor { player, _ ->
                plugin.launch {
                    plugin.teleportHub(clients.getClient(player) as ClientHub)
                }
            }
        }
    }
}