package com.github.rushyverse.hub.commannds

import com.github.rushyverse.api.extension.asComponent
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.api.player.getTypedClient
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import kotlin.random.Random

class MessageCommand {

    companion object {
        const val COMMAND_NAME = "message"
        private val conversations: MutableMap<String, String> = mutableMapOf()

        fun runMessageCommand(plugin: Hub, sender: Player, target: Player, message: String) {
            plugin.launch {
                val client = plugin.clientManager.getTypedClient<ClientHub>(sender)
                if (target == sender) {
                    val answers = plugin.translator.get("command.message.send.oneself", client.lang().locale)
                        .split("||")
                    val index = Random.nextInt(0, answers.size)
                    val randAnswer = answers[index].trim()

                    sender.sendMessage(randAnswer.asComponent())
                    return@launch
                }

                sendConversationMessage(plugin.translator, client, plugin.clientManager.getTypedClient(target), message)
            }
        }

        private suspend fun sendConversationMessage(
            translator: Translator,
            sender: ClientHub,
            target: ClientHub,
            message: String
        ) {
            val senderName = sender.requirePlayer().name
            val targetName = target.requirePlayer().name
            val senderPronoun = translator.get("command.message.pronoun.you", sender.lang().locale)
            sender.send("<gray>[<white>$senderPronoun <gray>-> <light_purple>$targetName<gray>]:<italic> $message".asComponent())

            val targetPronoun = translator.get("command.message.pronoun.you", target.lang().locale)
            target.send("<gray>[<light_purple>$senderName<gray> -> <white>$targetPronoun<gray>]:<italic> $message".asComponent())

            conversations.putIfAbsent(senderName, targetName)
        }
    }

    fun register(plugin: Hub) {

        val clients: ClientManager by inject(plugin.id)
        val translator: Translator by inject(plugin.id)

        commandAPICommand("message") {
            withAliases("msg")

            playerExecutor { sender, _ ->
                sender.sendMessage("You ran /message")
            }

            playerArgument("player") // Defines a new PlayerArgument("player")
            greedyStringArgument("message") // Defines a new GreedyStringArgument("msg")
            playerExecutor { sender, args -> // Command can be executed by anyone and anything (such as entities, the console, etc.)
                val target: Player = args[0] as Player
                val message: String = args[1] as String

                runMessageCommand(plugin, sender, target, message)


            }

        }

        commandAPICommand("reply") {
            aliases = arrayOf("r")
            greedyStringArgument("message")
            playerExecutor { sender, args ->
                val message = args[0] as String
                val storedLastTarget = conversations[sender.name]
                plugin.launch {
                    val client = clients.getTypedClient<ClientHub>(sender)

                    if (storedLastTarget == null) {
                        sender.sendMessage(
                            translator.getComponent("command.message.reply.nobody", client.lang().locale)
                                .color(NamedTextColor.RED)
                        )
                        return@launch
                    }

                    sender.performCommand("message $storedLastTarget $message")
                }
            }
        }
    }
}