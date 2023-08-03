package com.github.rushyverse.hub.commannds

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.hub.Hub
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

class LanguagesCommand {

    suspend fun register(plugin: Hub) {
        val clients: ClientManager by inject(plugin.id)

        commandAPICommand("languages") {
            aliases = arrayOf("lang")
            playerExecutor { player, _ ->

                plugin.launch {
                   plugin.langGui.open(clients.getClient(player))
                }
            }
        }
    }
}