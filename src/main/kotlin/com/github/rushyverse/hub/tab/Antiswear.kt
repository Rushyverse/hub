package com.github.rushyverse.hub.tab

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class Antiswear : Listener {

    private val swearWords = SwearWords.words

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val message = event.message
        if (containsSwearWord(message)) {
            event.isCancelled = true
            event.player.sendMessage("Please refrain from using inappropriate language.")
        }
    }

    private fun containsSwearWord(message: String): Boolean {
        // Check using regex pattern
        val pattern = "\\b(${swearWords.joinToString("|")})\\b".toRegex(RegexOption.IGNORE_CASE)
        if (pattern.containsMatchIn(message)) {
            return true
        }

        // Check using split words
        val words = message.split("\\s+".toRegex())
        for (word in words) {
            for (swearWord in swearWords) {
                if (word.toLowerCase().contains(swearWord.toLowerCase())) {
                    return true
                }
            }
        }
        return false
    }
}

/* val words = message.split("\\s+.".toRegex())
        for (word in words) {
            for (swearWord in swearWords) {
                if (word.toLowerCase().contains(swearWord)) {
                    return true
                }
            }
        }
        return false
    }
}
 */