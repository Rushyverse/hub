package com.github.rushyverse.hub.listener

import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.config.HotbarConfig
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

class HotbarItemsListener(
    override val plugin: Hub,
    private val hotbarConfig: HotbarConfig = plugin.config.hotbar,
) : ListenerHub(plugin) {
    val clients: ClientManager by inject(plugin.id)

    @EventHandler
    suspend fun onInteractItem(event: PlayerInteractEvent) {
        val player = event.player
        val world = player.world
        val item = event.item

        if (world != this.world || item == null || event.action.isLeftClick) {
            return
        }

        event.cancelIfNotAllowed(player)

        if (item.type == hotbarConfig.navigatorItem.type) {
            val client = clients.getClient(player) as ClientHub
            plugin.navigatorGui.open(client)
        }
    }
}