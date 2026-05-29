package com.github.rushyverse.hub.commands.players

import com.github.rushyverse.api.game.SharedGameData
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.hub.Hub
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.optionalArgument
import net.kyori.adventure.text.Component.text

class DataCommand
    (
    private val plugin: Hub,
) {
    private val clients: ClientManager by inject(plugin.id)
    private val sharedData: SharedGameData by inject(plugin.id)

    fun register() {
        commandAPICommand("data") {

            optionalArgument(StringArgument("padd"))
            optionalArgument(StringArgument("type"))
            optionalArgument(IntegerArgument("id"))
            anyExecutor { sender, args ->
                sender.sendMessage(
                    text(sharedData.games.toString())
                )

                val arg = args[0] as String
                if (arg.equals("padd")){

                    val type = args[1] as String

                    val id = args[2] as Int

                    val gameData = sharedData.games.find { it.id == id }
                    if (gameData != null){
                        gameData.apply {
                            players += 1
                        }

                        sharedData.saveUpdate(gameData)
                    }

                }
            }
        }
    }
}