package com.github.rushyverse.hub.gui.collectible.runnables

import com.github.rushyverse.api.player.Client
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

class BlackForceParticleTask(private val client : Client) : BukkitRunnable() {

    private var angle = 0.0
    private var heightOffset = 0.0
    private var heightDirection = 1

    override fun run() {
            val player = client.player
            if (player == null || !player.isOnline) {
            cancel()
            return
        }

        val location = player.location.clone()

        val radius = 1.5
        val x = radius * cos(Math.toRadians(angle))
        val z = radius * sin(Math.toRadians(angle))

        val yOffset = player.eyeHeight - 0.5
        location.add(x, yOffset + heightOffset, z)

        location.world.spawnParticle(Particle.REDSTONE, location, 1, 0.0, 0.0, 0.0, 1.0, Particle.DustOptions(Color.BLACK, 1.0f))

        location.world.spawnParticle(Particle.REDSTONE, location, 1, 0.0, 0.0, 0.0, 1.0, Particle.DustOptions(Color.PURPLE, 1.0f))

        angle += 10.0

        heightOffset += 0.05 * heightDirection

        if (heightOffset >= 1.0 || heightOffset <= -1.0) {
            heightDirection *= -1
        }
    }
}