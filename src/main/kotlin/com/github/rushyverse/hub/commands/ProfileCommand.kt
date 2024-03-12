package com.github.rushyverse.hub.commands

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import net.kyori.adventure.text.format.NamedTextColor

class ProfileCommand {

    fun register(plugin: Hub) {

        val clients: ClientManager by inject(plugin.id)

        commandAPICommand("profile") {
            playerExecutor { player, _ ->

                val world = player.world

                plugin.launch {
                    val client = clients.getClient(player) as ClientHub

                    if (world != plugin.world) {
                        val notAllowedMessage = plugin.translator.getComponent(
                            "not.allowed.outside.hub",
                            client.lang().locale
                        ).color(NamedTextColor.RED)
                        client.send(notAllowedMessage)
                        return@launch
                    }

//                    val head = ItemStack(Material.PLAYER_HEAD).apply {
//                        itemMeta = (itemMeta as SkullMeta).apply {
//                            owningPlayer = Bukkit.getOfflinePlayer(player.uniqueId)
//                        }
//                    }

                    plugin.profileGui.open(client)
                }

            }
        }
    }
}