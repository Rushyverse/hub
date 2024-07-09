package com.github.rushyverse.hub.extension

import org.bukkit.entity.Player
import java.util.HashMap
import java.util.UUID

class SimpleCooldown {

    val cooldowns: HashMap<UUID, Double> = HashMap()

    fun setCooldown(player: Player, seconds: Int) {
        val delay = System.currentTimeMillis().toDouble() + seconds * 1000
        cooldowns[player.uniqueId] = delay
    }

    /**
     * Check the cooldown of a player.
     * @param player
     * @return true if the player
     */
    fun check(player: Player): Boolean {
        return getStartTime(player) <= System.currentTimeMillis()
    }

    fun getStartTime(player: Player): Double {
        return cooldowns.getOrDefault(player.uniqueId, 0.0)
    }
}
