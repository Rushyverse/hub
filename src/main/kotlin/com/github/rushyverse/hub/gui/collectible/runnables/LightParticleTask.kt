package com.github.rushyverse.hub.gui.collectible.runnables

import com.github.rushyverse.api.player.Client
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class LightParticleTask(
    private val client: Client
) : BukkitRunnable() {

    private var step = 0

    override fun run() {
        val player = client.player
        if (player == null) {
            cancel()
            return
        }

        if (step >= 360) {
            step = 0;
            // this.cancel()
           // return
        }

        val radians = Math.toRadians(step.toDouble())
        val x = cos(radians)
        val z = sin(radians)

        val location = player.location
        val spawnLocation = location.clone().add(x, 1.0, z)

        val randomColor = Color.fromRGB(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        val dustOptions = Particle.DustOptions(randomColor, 1.0f)

        location.world.spawnParticle(Particle.REDSTONE, spawnLocation, 1, 0.0, 0.0, 0.0, 1.0, dustOptions)
        // location.world.spawnParticle(Particle.REDSTONE, spawnLocation, 1, 0.0, 0.0, 0.0, 1.0, Particle.DustOptions(Color.WHITE, 1.0f))

        step += 10

    }
}
