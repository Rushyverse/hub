package com.github.rushyverse.hub.commands.friends

import com.github.rushyverse.core.data.FriendService
import com.github.rushyverse.hub.Hub
import com.github.shynixn.mccoroutine.bukkit.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import org.bukkit.Bukkit

class FriendCommand(private val friendService: FriendService) {

    fun register(plugin: Hub) {
        commandAPICommand("friend") {
            withAliases("f")

            subcommand("add") {
                argument(StringArgument("playerName"))
                playerExecutor { player, args ->
                    val friendName = args.get("playerName") as String
                    val playerUUID = player.uniqueId

                    val friendPlayer = Bukkit.getPlayerExact(friendName)
                    if (friendPlayer == null) {
                        player.sendMessage("Player $friendName could not be found.")
                        return@playerExecutor
                    }
                    val friendUUID = friendPlayer.uniqueId

                    plugin.launch {
                        try {
                            val result = friendService.addFriend(playerUUID, friendUUID)
                            if (result) {
                                player.sendMessage("${friendPlayer.name} has been added to your friends list!")
                            } else {
                                player.sendMessage("${friendPlayer.name} is already on your friends list.")
                            }
                        } catch (e: Exception) {
                            player.sendMessage("An error occurred while adding your friend: ${e.message}")
                        }
                    }
                }
            }

            subcommand("list") {
                playerExecutor { player, _ ->
                    val playerUUID = player.uniqueId

                    plugin.launch {
                        try {
                        al    v friendsList: List<*>? = friendService.getFriends(playerUUID) as? List<*>
                            if (!friendsList.isNullOrEmpty()) {
                                val friendsString = friendsList.joinToString(", ")
                                player.sendMessage("Your friends: $friendsString")
                            } else {
                                player.sendMessage("You have no friends added.")
                            }
                        } catch (e: Exception) {
                            player.sendMessage("An error occurred while retrieving your friends list: ${e.message}")
                        }
                    }
                }
            }
        }
    }
}