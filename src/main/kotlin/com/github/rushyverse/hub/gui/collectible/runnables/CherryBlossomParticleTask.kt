package com.github.rushyverse.hub.gui.collectible.runnables

import com.github.rushyverse.api.player.Client
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin

class CherryBlossomParticleTask(private val client : Client) : BukkitRunnable() {

    private var angle = 0.0
    private var radius = 0.0
    private var ascending = true

    override fun run() {
        val player = client.player ?: return
        val playerLocation = player.location

        val x = radius * cos(angle)
        val z = radius * sin(angle)

        val spawnLocation = playerLocation.clone().add(x, 0.0, z)

        val particleCount = 20

        for (i in 0 until particleCount) {
            val color = Color.fromRGB(
                (Math.random() * 255).toInt(),
                (Math.random() * 255).toInt(),
                (Math.random() * 255).toInt()
            )

            playerLocation.world.spawnParticle(
                Particle.REDSTONE,
                spawnLocation,
                1,
                0.0, 0.0, 0.0,
                0.0,
                Particle.DustOptions(color, 1.0f)
            )
        }

        if (ascending) {
            angle += Math.PI / 8
            radius += 0.1
        } else {
            angle -= Math.PI / 8
            radius -= 0.1
        }

        if (radius >= 5.0) {
            ascending = false
        } else if (radius <= 0.0) {
            ascending = true
        }
    }
}