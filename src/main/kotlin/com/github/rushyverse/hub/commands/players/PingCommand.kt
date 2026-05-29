package com.github.rushyverse.hub.commands.players

import com.github.rushyverse.api.extension.asComponent
import com.github.rushyverse.api.extension.toText
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOnePlayer
import dev.jorel.commandapi.kotlindsl.playerExecutor
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class PingCommand {

    fun register() {
        commandTree("ping") {
            playerExecutor { player, args ->
                val message = text("Your ping is ", NamedTextColor.LIGHT_PURPLE).append(
                    "${player.spigot().ping}ms".asComponent().color(NamedTextColor.YELLOW)
                )
                player.sendMessage(message)
            }
            entitySelectorArgumentOnePlayer("target") {
                playerExecutor { player, args ->
                    val target: Player = args["target"] as Player
                    val targetName = target.name().toText()
                    val message = text(
                        "${targetName}'s ping is ",
                        NamedTextColor.LIGHT_PURPLE
                    ).append("${target.spigot().ping}ms".asComponent().color(NamedTextColor.YELLOW))
                    player.sendMessage(message)
                }
            }
        }
    }
}