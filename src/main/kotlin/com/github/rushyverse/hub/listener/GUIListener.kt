package com.github.rushyverse.hub.listener

import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.gui.commons.GUI
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class GUIListener(
    override val plugin: Hub,
    val listOfGui: Set<GUI>
) : ListenerHub(plugin) {

    val clients: ClientManager by inject(plugin.id)

    @EventHandler
    suspend fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val world = player.world

        if (world != plugin.world) {
            return
        }

        event.cancelIfNotAllowed(player)

        val item = event.currentItem ?: return
        val client = clients.getClient(player) as ClientHub

        listOfGui.firstOrNull { it.viewers.contains(client) }?.onClick(client, item, event.click)
    }

    @EventHandler
    suspend fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        val world = player.world

        if (world != plugin.world) {
            return
        }

        clients.getClientOrNull(player)?.apply {
            listOfGui.forEach {
                if (it.viewers.contains(this)) {
                    it.close(this)
                    player.sendMessage("removed from §C${it.titleKey}")
                }
            }
        }

    }
}