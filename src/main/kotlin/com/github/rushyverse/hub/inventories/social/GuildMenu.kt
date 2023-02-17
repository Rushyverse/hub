package com.github.rushyverse.hub.inventories.social

import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.extension.setPreviousButton
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class GuildMenu(
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player,
    private val previousInventory: Inventory? = null
) : IMenu {

    override suspend fun build(): Inventory {
        val title = translationsProvider.translate("guild_menu_title", locale, HubServer.BUNDLE_HUB)
        val inv = Inventory(InventoryType.CHEST_4_ROW, title)
        inv.setItemStack(
            1, createItem(
                Material.ENDER_EYE,
                "Guild #1",
                "§ETop 1 du Classement"
            )
        )

        previousInventory?.let {
            inv.setPreviousButton(30, it)
        }
        inv.setCloseButton(32)
        return inv
    }

    private fun createItem(material: Material, name: String, description: String): ItemStack {
        return ItemStack.builder(material)
            .displayName(Component.text(name))
            .lore(Component.text(description))
            .build()
    }
}