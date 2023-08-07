package com.github.rushyverse.hub.commannds

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import net.kyori.adventure.text.Component

class LanguagesCommand {

    suspend fun register(plugin: Hub) {
        val clients: ClientManager by inject(plugin.id)

        commandAPICommand("languages") {
            aliases = arrayOf("lang")
            playerExecutor { player, _ ->

                plugin.launch {
                    val client = clients.getClient(player)
                    if (player.world != plugin.world){
                        player.sendMessage(
                            Component.text(
                                Hub.translator.translate(
                                    "not.allowed.outside.hub",
                                    client.lang.locale,
                                    BUNDLE_HUB
                                )
                            )
                        )
                        return@launch
                    }

                   plugin.langGui.open(client)
                }
            }
        }
    }
}