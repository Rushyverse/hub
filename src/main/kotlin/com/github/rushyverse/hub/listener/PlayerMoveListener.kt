package com.github.rushyverse.hub.listener

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerMoveEvent

class PlayerMoveListener(
    private val limitY : Double,
    private val spawnPoint: Pos,
) : EventListener<PlayerMoveEvent> {

    override fun eventType(): Class<PlayerMoveEvent> {
        return PlayerMoveEvent::class.java
    }

    override fun run(event: PlayerMoveEvent): EventListener.Result {
        val player = event.player
        val pos = player.position

        if (pos.y <= limitY && !player.isDead) {
            player.teleport(spawnPoint)
            return EventListener.Result.SUCCESS
        }

        player.getAcquirable<Player>().sync { player ->
            player.isAllowFlying = player.gameMode == GameMode.CREATIVE || player.isAllowFlying || event.isOnGround
        }


        return EventListener.Result.SUCCESS
    }
}