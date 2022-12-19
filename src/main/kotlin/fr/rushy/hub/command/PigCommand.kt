package fr.rushy.hub.command

import fr.rushy.hub.mount.PigMount
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

/**
 * Command to summon a pig at the player's position.
 */
class PigCommand : Command("pig") {

    init {
        setDefaultExecutor { sender, _ ->
            val player = sender as? Player ?: return@setDefaultExecutor
            val instance = player.instance ?: return@setDefaultExecutor

            val pig = PigMount()
            pig.setInstance(instance, player.position)

            pig.addPassenger(player)
        }
    }
}