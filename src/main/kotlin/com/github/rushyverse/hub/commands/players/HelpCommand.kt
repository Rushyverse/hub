package com.github.rushyverse.hub.commands.players

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

class HelpCommand {

    fun register() {
        // Register help command
        commandAPICommand("hel") {
            withAliases("h")
            playerExecutor { player, _ ->
                player.sendMessage("""
                    §6Available Commands:
                    §b/help§7, §b/h§r - Displays this help message.
                    §b/hub§7, §b/lobby§7, §b/l§7, §b/spawn§r - Teleports you to the hub.
                    §b/ping §7[§eplayer§7]§r - Displays your ping or the specified player's ping.
                    §b/language §7[§elang§7]§r, §b/lang §7[§elang§7]§r - Sets your preferred language.
                    §b/message §7[§cplayer§7] §7[§cmessage§7]§7, §b/msg §7[§cplayer§7] §7[§cmessage§7]§r - Sends a private message to the specified player.
                    §b/reply §7[§cmessage§7]§r, §b/r §7[§cmessage§7]§r - Replies to the last received message, if available.
                """.trimIndent())
            }
        }
    }
}
