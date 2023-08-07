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

class VisibilityCommand {

    fun register(plugin: Hub) {
        val clients: ClientManager by inject(plugin.id)
        val commandConfig = plugin.config.visibilityCommand
        val itemConfig = commandConfig.item

        commandAPICommand("visibility") {
            aliases = arrayOf("togglevisibility", "tgv")
            playerExecutor { player, _ ->

                val world = player.world

                plugin.launch {
                    val client = clients.getClient(player) as ClientHub

                    if (world != plugin.world) {
                        val notAllowedMessage = Hub.translator.translate(
                            "not.allowed.outside.hub",
                            client.lang.locale,
                            BUNDLE_HUB
                        )
                        client.send(Component.text(notAllowedMessage, NamedTextColor.RED))
                        return@launch
                    }

                    val newVisibilityState = !client.canSeePlayers

                    client.canSeePlayers(newVisibilityState, plugin)

                    val message = Hub.translator.translate(
                        "visibility.players.$newVisibilityState",
                        client.lang.locale,
                        BUNDLE_HUB
                    )

                    client.send(Component.text(message, NamedTextColor.GREEN))

                    if (itemConfig.enabled) {
                        val item = player.inventory.getItem(itemConfig.slot)
                        player.inventory.setItem(itemConfig.slot,
                            item?.apply {
                                type = if (newVisibilityState)
                                    itemConfig.materialOn
                                else itemConfig.materialOff
                            }
                        )

                    }
                }

            }
        }
    }
}