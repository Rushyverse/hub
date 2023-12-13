package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.data.Cosmetic
import com.github.rushyverse.hub.extension.GUIUtils
import com.github.rushyverse.hub.extension.ItemStack
import com.github.rushyverse.hub.gui.commons.GUI
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.*

abstract class CosmeticGUI(titleKey: String, guiSize: Int) : GUI(titleKey, guiSize) {

    companion object {

        const val COSMETIC_PLAYER_INV_SLOT = 5

    }

    protected abstract val cosmetics: List<Pair<Int, Cosmetic>>

    override suspend fun applyItems(client: Client, inv: Inventory) {
        val locale = client.lang().locale

        cosmetics.forEach { (slot, cosmetic) ->
            inv.setItem(slot, createCosmeticItem(cosmetic, locale))
        }

        inv.setItem(48, GUIUtils.createBackward(locale))
        inv.setItem(50, GUIUtils.createUnequip(locale))
    }

    override suspend fun onClick(client: Client, item: ItemStack, event: InventoryClickEvent) {
        val player =  client.player ?: return

        val slot = event.slot
        // We check from the slot for optimization (instead of check similarity of Item)
        // The inventory must not be changed
        val cosmetic = cosmetics.find { it.first == slot }?.second ?: return

        val locale = client.lang().locale

        val cosmeticItem = createCosmeticItem(cosmetic, locale)
        player.closeInventory()

        val itemName = item.displayName()

        setItem(cosmeticItem, player.inventory, event)

        client.send(translator.getComponent(
            "shop.selected", client.lang().locale).append(itemName)
        )
    }

    private fun createCosmeticItem(cosmetic: Cosmetic, locale: Locale): ItemStack {
        return ItemStack(
            type = cosmetic.material,
            name = "shop.${cosmetic.name.lowercase()}.name",
            description = "shop.${cosmetic.name.lowercase()}.desc",
            locale = locale,
            translator = translator
        )
    }

    protected abstract fun setItem(cosmeticItem: ItemStack, inventory: PlayerInventory, event: InventoryClickEvent)
}