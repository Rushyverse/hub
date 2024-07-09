package com.github.rushyverse.hub.commands.staffonly

import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.ChatColor
import kotlin.random.Random

class ScrambleCommand {
    fun register() {
        commandAPICommand("scramble") {
            withAliases("scram")
            playerArgument("word")
            playerExecutor { player, args ->
                val word = args[0]
                val scrambledWord = scrambleWord(word.toString())
                player.sendMessage(ChatColor.YELLOW.toString() + "Scrambled word: " + ChatColor.WHITE + scrambledWord)
            }
        }
    }

    private fun scrambleWord(word: String): String {
        val characters = word.toCharArray()
        val random = Random
        for (i in characters.indices) {
            val randomIndex = random.nextInt(characters.size)
            val temp = characters[i]
            characters[i] = characters[randomIndex]
            characters[randomIndex] = temp
        }
        return String(characters)
    }
}