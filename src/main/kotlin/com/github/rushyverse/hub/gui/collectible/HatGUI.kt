package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.hub.data.Cosmetic
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class HatGUI : CosmeticGUI("gui.hats.title", 54) {

     override val cosmetics = listOf(
        10 to Cosmetic.Hats.AstroHat,
        11 to Cosmetic.Hats.RainbowHat,
        12 to Cosmetic.Hats.LampHat,
        13 to Cosmetic.Hats.SuperHat,
        14 to Cosmetic.Hats.CrimsonHat
    )

    override fun setItem(cosmeticItem: ItemStack, inventory: PlayerInventory, event: InventoryClickEvent) {
        inventory.helmet = cosmeticItem
    }
}