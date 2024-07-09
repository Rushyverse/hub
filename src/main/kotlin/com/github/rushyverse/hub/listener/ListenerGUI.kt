package com.github.rushyverse.hub.listener

import com.github.rushyverse.api.extension.event.cancel
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class ListenerGUI : Listener {

    @EventHandler
    fun onInvClick(event: InventoryClickEvent){
        val inv = event.inventory
        val player = event.whoClicked as Player

        event.cancel()
    }
}