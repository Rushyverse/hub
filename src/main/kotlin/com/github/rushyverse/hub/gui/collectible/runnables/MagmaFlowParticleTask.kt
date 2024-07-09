package com.github.rushyverse.hub.gui.collectible.runnables

import com.github.rushyverse.api.player.Client
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin


class MagmaFlowParticleTask(private val client: Client) : BukkitRunnable() {

    private var step = 0

    override fun run() {
        val player = client.player
        if (player == null) {
            cancel()
            return
        }

        if (step >= 360) {
            step = 0
        }

        val radians1 = Math.toRadians(step.toDouble())
        val radians2 = Math.toRadians(step.toDouble() + 180) // Décalage de 180 degrés pour le deuxième anneau
        val x1 = cos(radians1)
        val z1 = sin(radians1)
        val x2 = cos(radians2)
        val z2 = sin(radians2)

        val location = player.location
        val spawnLocation1 = location.clone().add(x1, 1.0, z1)
        val spawnLocation2 = location.clone().add(x2, 1.0, z2)

        location.world.spawnParticle(Particle.FLAME, spawnLocation1, 1, 0.0, 0.0, 0.0, 0.0)
        location.world.spawnParticle(Particle.FLAME, spawnLocation2, 1, 0.0, 0.0, 0.0, 0.0)

        step += 10
    }
}