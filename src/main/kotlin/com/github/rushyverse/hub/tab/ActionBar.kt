package com.github.rushyverse.hub.tab

import com.github.rushyverse.api.extension.toText
import com.github.rushyverse.hub.Hub
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class ActionBar(val plugin: Hub) : Listener {

    private val timeBetweenMessage = 5
    private var actionBarPaused = false
    private val highlightColor = NamedTextColor.YELLOW

    private var currentCharHighlightIndex = 0
    private var currentMessageIndex = 0

    private var direction = 1 // 1: forward, -1: backward
    private val listOfComponents = listOf(
        text("Welcome to Rushy", NamedTextColor.LIGHT_PURPLE),
        text("Enjoy your stay", NamedTextColor.RED),
        text("Store at www.rushy.space", NamedTextColor.YELLOW)
    )

    init {
        // Print multiple messages
        var time = 0
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (time == timeBetweenMessage) {
                time = 0
                if ((listOfComponents.size - 1) == currentMessageIndex) {
                    currentMessageIndex = 0
                } else {
                    currentMessageIndex++
                }

                actionBarPaused = !actionBarPaused // No message during {timeBetweenMessage} seconds
            } else {
                time++
            }
        }, 0L, 20L)
        // Schedule the task to run every second
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (!actionBarPaused)
                updateActionBar()
        }, 0L, 5L) // 20 ticks equal to 1 second
    }

    fun updateActionBar() {
        val component = listOfComponents[currentMessageIndex]
        val highlightMessage = text().also {
            for ((index, char) in component.color(null).toText().withIndex()) {
                // println("$index -> $char")
                val highlight = if (index == currentCharHighlightIndex) highlightColor else component.color()
                it.append(text(char, highlight))
            }
        }

        // Increment index for the next update
        currentCharHighlightIndex += direction

        // Reverse direction when reaching the ends
        if (currentCharHighlightIndex == 0 || currentCharHighlightIndex == component.color(null).toText().length - 1) {
            direction *= -1
        }

        // Send the updated message to all players
        for (player in plugin.world.players) {
            player.sendActionBar(highlightMessage)
        }
    }
}