package com.github.rushyverse.hub.gui.commons

import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.Hub.Companion.translator
import com.github.rushyverse.api.player.Client
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Abstract representation of a GUI.
 * @property titleKey String the translation title key of the GUI.
 * @property size Int must be mulitple of 9, can't be superior to 54.
 * @property viewers MutableList<Client>
 */
abstract class GUI(
    val titleKey: String,
    val size: Int,
) {

    val viewers: MutableList<Client> = mutableListOf()

    suspend fun open(client: Client) {
        val translatedTitle = if (titleKey.contains(".")) {
            translator.translate(titleKey, client.lang.locale, BUNDLE_HUB)
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

    abstract suspend fun onClick(client: Client, item: ItemStack, clickType: ClickType)
}