package com.github.rushyverse.hub.gui.collectible.runnables

import com.github.rushyverse.api.player.Client
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class BloodDripsParticleTask(private val client : Client) : BukkitRunnable() {

    override fun run() {
        val player = client.player
        if (player == null || !player.isOnline) {
            cancel()
            return
        }

        val location = player.location.clone()
        val random = Random.Default

        val xOffset = random.nextDouble(-0.2, 0.2)
        val zOffset = random.nextDouble(-0.2, 0.2)
        location.add(xOffset, 1.8, zOffset)

        val particleCount = 5
        val particleColor = Color.fromRGB(165, 0, 0)
        val particleSize = 0.1
        for (i in 0 until particleCount) {
            val offsetX = random.nextDouble(-0.1, 0.1)
            val offsetY = random.nextDouble(-0.1, 0.1)
            val offsetZ = random.nextDouble(-0.1, 0.1)
            location.world.spawnParticle(
                Particle.REDSTONE,
                location.clone().add(offsetX, offsetY, offsetZ),
                1,
                0.0, -0.2, 0.0,
                particleSize,
                Particle.DustOptions(particleColor, 1.0f)
            )
        }
    }
}