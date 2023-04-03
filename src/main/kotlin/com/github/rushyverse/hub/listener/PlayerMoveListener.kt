package com.github.rushyverse.hub.listener

import com.github.rushyverse.hub.configuration.AreaConfiguration
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerMoveEvent

class PlayerMoveListener(
    private val area: AreaConfiguration
) : EventListener<PlayerMoveEvent> {

    override fun eventType(): Class<PlayerMoveEvent> {
        return PlayerMoveEvent::class.java
    }

    override fun run(event: PlayerMoveEvent): EventListener.Result {
        val player = event.player
        val pos = player.position

        if (pos.y <= area.limitY && !player.isDead) {
            player.teleport(area.spawnPoint)
            return EventListener.Result.SUCCESS
        }

        event.player.getAcquirable<Player>().sync { player ->
            player.isAllowFlying = player.gameMode == GameMode.CREATIVE || player.isAllowFlying || event.isOnGround
        }
        return EventListener.Result.SUCCESS
    }
}