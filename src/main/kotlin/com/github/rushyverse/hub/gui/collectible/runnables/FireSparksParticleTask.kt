package com.github.rushyverse.hub.gui.collectible.runnables

import com.github.rushyverse.api.player.Client
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable

class FireSparksParticleTask(private val client : Client) : BukkitRunnable() {

    private val particlesCount = 10
    private val sideLength = 2.0

    override fun run() {
        val player = client.player
        if (player == null || !player.isOnline) {
            cancel()
            return
        }

        val location = player.location.clone()
        location.add(0.0, 2.0, 0.0)


        val spacing = sideLength / particlesCount


        val offset = sideLength / 2

        for (i in 0 until particlesCount) {
            val x = -offset + i * spacing
            val spawnLocation = location.clone().add(x, 0.0, offset)
            spawnParticle(spawnLocation)
        }

        for (i in 0 until particlesCount) {
            val x = -offset + i * spacing
            val spawnLocation = location.clone().add(x, 0.0, -offset)
            spawnParticle(spawnLocation)
        }

        for (i in 1 until particlesCount - 1) {
            val z = -offset + i * spacing
            val spawnLocation = location.clone().add(-offset, 0.0, z)
            spawnParticle(spawnLocation)
        }

        for (i in 1 until particlesCount - 1) {
            val z = -offset + i * spacing
            val spawnLocation = location.clone().add(offset, 0.0, z)
            spawnParticle(spawnLocation)
        }
    }

    private fun spawnParticle(location: Location) {
        location.world.spawnParticle(Particle.REDSTONE, location, 1, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(Color.YELLOW, 1.0f))
    }
}