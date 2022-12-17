package fr.rushy.hub.listener

import fr.rushy.hub.permission.CustomPermission
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.permission.Permission

class PlayerLoginListener(private val instanceContainer: InstanceContainer) : EventListener<PlayerLoginEvent> {

    override fun eventType(): Class<PlayerLoginEvent> {
        return PlayerLoginEvent::class.java
    }

    override fun run(event: PlayerLoginEvent): EventListener.Result {
        event.player.getAcquirable<Player>().sync { player ->
            event.setSpawningInstance(instanceContainer)
            player.respawnPoint = Pos(0.0, 100.0, 0.0)

            sequenceOf(
                CustomPermission.GIVE,
                CustomPermission.STOP_SERVER,
                CustomPermission.GAMEMODE,
                CustomPermission.KICK
            ).map { Permission(it) }.forEach(player::addPermission)
        }
        return EventListener.Result.SUCCESS
    }
}