package com.github.rushyverse.hub.listener

import com.github.rushyverse.api.extension.event.cancelIf
import com.github.rushyverse.hub.Hub
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Listener

abstract class ListenerHub(
    open val plugin: Hub,
    val world: World = plugin.world
) : Listener {

    // Verify that the player is allowed to do something on the server
    protected fun isNotAllowed(player: Player) = player.world == world && player.gameMode != GameMode.CREATIVE

    // Extension function to cancel directly an event if the player is not allowed to do something
    protected fun Cancellable.cancelIfNotAllowed(player: Player) = cancelIf { isNotAllowed(player) }
}