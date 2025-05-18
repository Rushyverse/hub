package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.data.CosmeticData
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class HatGUI(plugin: Hub, translator: Translator) : CosmeticGUI(plugin, translator, "gui.hats.title") {

    override val cosmetics = listOf(
        CosmeticDataInventory(10, CosmeticData.Hats.AstroHat) { },
        CosmeticDataInventory(11, CosmeticData.Hats.RainbowHat()) { },
        CosmeticDataInventory(12, CosmeticData.Hats.LampHat) { },
        CosmeticDataInventory(13, CosmeticData.Hats.SuperHat) { },
        CosmeticDataInventory(14, CosmeticData.Hats.CrimsonHat) { }
    )

    override fun setItem(
        clickedItem: ItemStack,
        cosmeticData: CosmeticDataInventory?,
        inventory: PlayerInventory,
        event: InventoryClickEvent
    ) {
        inventory.helmet = clickedItem

        cosmeticData?.cosmetic?.runEffect(event.whoClicked as Player, plugin as Hub, 15L)
    }

    override fun onUnselect(inventory: PlayerInventory, client: ClientHub) {
        inventory.helmet = null
    }

    override fun isAlreadySelected(inventory: PlayerInventory, cosmeticItem: ItemStack): Boolean {
        return inventory.helmet == cosmeticItem
    }
}