package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.hub.data.Cosmetic
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class GadgetGUI : CosmeticGUI("gui.gadgets.title", 54) {

    override val cosmetics = listOf(
        10 to Cosmetic.Gadgets.GrapplingHook,
        11 to Cosmetic.Gadgets.SnowWand,
        12 to Cosmetic.Gadgets.FireworkBow,
        13 to Cosmetic.Gadgets.EnderButt,
        14 to Cosmetic.Gadgets.KnockbackStick
    )

    override fun setItem(cosmeticItem: ItemStack, inventory: PlayerInventory, event: InventoryClickEvent) {
        inventory.setItem(CosmeticGUI.COSMETIC_PLAYER_INV_SLOT, cosmeticItem)
    }
}