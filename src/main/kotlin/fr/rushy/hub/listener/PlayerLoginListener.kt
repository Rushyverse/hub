package fr.rushy.hub.listener

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.InstanceContainer

class PlayerLoginListener(private val instanceContainer: InstanceContainer) : EventListener<PlayerLoginEvent> {

    override fun eventType(): Class<PlayerLoginEvent> {
        return PlayerLoginEvent::class.java
    }

    override fun run(event: PlayerLoginEvent): EventListener.Result {
        event.player.getAcquirable<Player>().sync { player ->
            event.setSpawningInstance(instanceContainer)
            player.respawnPoint = Pos(0.0, 100.0, 0.0)
        }
        return EventListener.Result.SUCCESS
    }
}