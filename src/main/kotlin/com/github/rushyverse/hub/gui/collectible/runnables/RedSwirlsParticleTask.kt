import com.github.rushyverse.api.player.Client
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class RedSwirlsParticleTask(
    private val client: Client
) : BukkitRunnable() {

    private var step = 0

    override fun run() {
        val player = client.player
        if (player == null || !player.isOnline) {
            cancel()
            return
        }

        if (step >= 360) {
            step = 0
        }

        // Angle pour la première particule (red)
        val radiansRed = Math.toRadians(step.toDouble())
        val xRed = cos(radiansRed)
        val zRed = sin(radiansRed)

        // Angle pour la deuxième particule (bleue)
        val radiansBlue = Math.toRadians(step.toDouble() + 120) // Ajouter 120 degrés pour les séparer
        val xBlue = cos(radiansBlue)
        val zBlue = sin(radiansBlue)

        // Angle pour la troisième particule (verte)
        val radiansPurple = Math.toRadians(step.toDouble() + 240) // Ajouter 240 degrés pour les séparer
        val xPurple = cos(radiansPurple)
        val zPurple = sin(radiansPurple)

        val location = player.eyeLocation // Utiliser eyeLocation pour obtenir l'emplacement des yeux du joueur

        val spawnLocationLava = location.clone().add(xRed, 0.0, zRed)
        val spawnLocationBlue = location.clone().add(xBlue, 0.0, zBlue)
        val spawnLocationPurple = location.clone().add(xPurple, 0.0, zPurple)


        // Spawn red particles around the player's head
        location.world.spawnParticle(Particle.REDSTONE, spawnLocationLava, 1, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(Color.RED, 1.0f))

        // Spawn blue particles around the player's head
        location.world.spawnParticle(Particle.REDSTONE, spawnLocationBlue, 1, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(Color.AQUA, 1.0f))

        // Spawn green particles around the player's head
        location.world.spawnParticle(Particle.REDSTONE, spawnLocationPurple, 1, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(Color.FUCHSIA, 1.0f))

        step += 10
    }
}
