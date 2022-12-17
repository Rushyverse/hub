package fr.rushy.hub.listener

import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerStartFlyingEvent

class PlayerStartFlyingListener : EventListener<PlayerStartFlyingEvent> {

    override fun eventType(): Class<PlayerStartFlyingEvent> {
        return PlayerStartFlyingEvent::class.java
    }

    override fun run(event: PlayerStartFlyingEvent): EventListener.Result {
        event.player.getAcquirable<Player>().sync { player ->
            if (player.gameMode == GameMode.CREATIVE) {
                return@sync
            }

            player.apply {
                isFlying = false
                isAllowFlying = false
                velocity = position.direction().mul(20.0).withY(20.0)
            }
        }

        return EventListener.Result.SUCCESS
    }
}