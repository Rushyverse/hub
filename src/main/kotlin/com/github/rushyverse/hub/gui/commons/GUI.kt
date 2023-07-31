package com.github.rushyverse.hub.gui.commons

import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.Hub.Companion.translationsProvider
import com.github.rushyverse.api.player.Client
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

abstract class GUI(
    val titleKey: String,
    val size: Int,
) {

    val viewers: MutableList<Client> = mutableListOf()

    fun open(client: Client) {
        val translatedTitle = if (titleKey.contains(".")) {
            translationsProvider.translate(titleKey, client.lang.locale, BUNDLE_HUB)
        } else titleKey
        val inv = Bukkit.createInventory(null, size, text(translatedTitle))
        client.requirePlayer().openInventory(inv)
        applyItems(client, inv)
        viewers.add(client)
    }

    fun close(client: Client) {
        viewers.remove(client)
        client.requirePlayer().closeInventory()
    }

    fun sync() {
        for (viewer in viewers) {
            applyItems(viewer, viewer.requirePlayer().openInventory.topInventory)
        }
    }

    abstract fun applyItems(client: Client, inv: Inventory)

    abstract fun onClick(client: Client, item: ItemStack, clickType: ClickType)
}