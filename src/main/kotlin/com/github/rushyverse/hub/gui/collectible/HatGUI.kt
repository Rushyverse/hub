package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.api.Plugin
import com.github.rushyverse.hub.data.Cosmetic
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class HatGUI(plugin: Plugin) : CosmeticGUI(plugin, "gui.hats.title") {

     override val cosmetics = listOf(
        CosmeticDataInventory(10, Cosmetic.Hats.AstroHat) { },
        CosmeticDataInventory(11, Cosmetic.Hats.RainbowHat) { },
        CosmeticDataInventory(12, Cosmetic.Hats.LampHat) { },
        CosmeticDataInventory(13, Cosmetic.Hats.SuperHat) { },
        CosmeticDataInventory(14, Cosmetic.Hats.CrimsonHat) { }
    )

    override fun setItem(cosmeticItem: ItemStack, inventory: PlayerInventory, event: InventoryClickEvent) {
        inventory.helmet = cosmeticItem
    }

    override fun removeItem(inventory: PlayerInventory) {
        inventory.helmet = null
    }

    override fun isSetItem(inventory: PlayerInventory, cosmeticItem: ItemStack): Boolean {
        return inventory.helmet == cosmeticItem
    }
}