package com.github.rushyverse.hub.commannds

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.client.ClientHub
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class MenuCommand {

    fun register(plugin: Hub) {
        val clients: ClientManager by inject(plugin.id)
        val commandConfig = plugin.config.visibilityCommand
        val itemConfig = commandConfig.item

        commandAPICommand("menu") {
            playerExecutor { player, _ ->

                val world = player.world

                plugin.launch {
                    val client = clients.getClient(player) as ClientHub

                    if (world != plugin.world) {
                        val notAllowedMessage = Hub.translationsProvider.translate(
                            "not.allowed.outside.hub",
                            client.lang.locale,
                            BUNDLE_HUB
                        )
                        client.send(Component.text(notAllowedMessage, NamedTextColor.RED))
                        return@launch
                    }

                    plugin.navigatorGui.open(client)
                }

            }
        }
    }
}