package com.github.rushyverse.hub.gui.collectible.runnables

import com.github.rushyverse.api.player.Client
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class GreenAuraParticleTask(
    private val client : Client
) : BukkitRunnable() {

    private var previousLocation: Location? = null // if not moving body = no particles
    private var previousDirection: Vector? = null // if not moving camera = no particles

    override fun run() {
        val player = client.player
        if (player == null || !player.isOnline) {
            cancel()
            return
        }

        val currentLocation = player.location
        val currentDirection = player.location.direction

        if (previousLocation != null && currentLocation == previousLocation && currentDirection == previousDirection) {
            return
        }

        previousLocation = currentLocation.clone()
        previousDirection = currentDirection.clone()

        // Spawn cloud trail particles behind the player
        currentLocation.world.spawnParticle(Particle.CLOUD, currentLocation, 20, 0.0, 0.0, 0.0, 0.1)
    }
}