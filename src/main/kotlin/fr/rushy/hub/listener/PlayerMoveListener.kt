package fr.rushy.hub.listener

import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerMoveEvent

class PlayerMoveListener : EventListener<PlayerMoveEvent> {

    override fun eventType(): Class<PlayerMoveEvent> {
        return PlayerMoveEvent::class.java
    }

    override fun run(event: PlayerMoveEvent): EventListener.Result {
        event.player.isAllowFlying = event.player.isAllowFlying || event.isOnGround
        return EventListener.Result.SUCCESS
    }
}