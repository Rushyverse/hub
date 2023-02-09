package com.github.rushyverse.hub.inventories.player

import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.IMenu
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class CosmeticsMenu(
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player
) : IMenu {

    override suspend fun build(): Inventory {
        val title = translationsProvider.translate("cosmetics_menu_title", locale, HubServer.BUNDLE_HUB)
        val inventory = Inventory(InventoryType.CHEST_1_ROW, title)

        inventory.addItemStack(ItemStack.of(Material.DIAMOND))

        return inventory
    }
}