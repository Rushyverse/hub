package com.github.rushyverse.hub.commands

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.api.translation.getComponent
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
                    val client = clients.getClient(player)
                    if (player.world != plugin.world) {
                        player.sendMessage(
                            plugin.translator.getComponent(
                                "not.allowed.outside.hub",
                                client.lang().locale,
                            )
                        )
                        return@launch
                    }

                    plugin.languageGui.open(client)
                }
            }
        }
    }
}