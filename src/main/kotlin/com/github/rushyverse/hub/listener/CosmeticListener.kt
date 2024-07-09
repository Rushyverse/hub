package com.github.rushyverse.hub.listener

import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.gui.GUIManager
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.gui.collectible.CosmeticGUI
import kotlinx.coroutines.flow.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class CosmeticListener(plugin: Plugin) : Listener {

    val clients: ClientManager by inject(plugin.id)

    val guiManager: GUIManager by inject(plugin.id)

    @EventHandler
    suspend fun onItemClickEvent(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val client = clients.getClient(event.player)
        guiManager.guis
            .asFlow()
            .filterIsInstance<CosmeticGUI>()
            .firstOrNull { it.onClickCosmetic(client as ClientHub, item) }
    }

}