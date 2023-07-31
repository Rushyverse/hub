package com.github.rushyverse.hub.listener

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.api.player.*
import com.github.rushyverse.hub.client.ClientHub
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class AuthenticationListener(
    private val hub: Hub
) : Listener {

    private val clients: ClientManager by inject(hub.id)

    @EventHandler
    suspend fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val client = clients.getClient(player) as ClientHub

        hub.teleportHub(client)
        event.joinMessage(null)
    }
}