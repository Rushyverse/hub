package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class rtfCosmeticsGUI(plugin: Hub, translator: Translator) : CosmeticGUI(plugin, translator, "gui.rtfcosmetics.title") {
    override val cosmetics: List<CosmeticDataInventory>
        get() = TODO("Not yet implemented")

    override fun setItem(
        clickedItem: ItemStack,
        cosmetic: CosmeticDataInventory?,
        player: PlayerInventory,
        event: InventoryClickEvent
    ) {
        TODO("Not yet implemented")
    }

    override fun onUnselect(inventory: PlayerInventory, client: ClientHub) {
        inventory.setItem(
            COSMETIC_PLAYER_INV_SLOT,
            null
        )

        client.particleTask?.cancel()?.also { client.particleTask = null }
    }

    override fun isAlreadySelected(inventory: PlayerInventory, cosmeticItem: ItemStack): Boolean {
        return inventory.getItem(COSMETIC_PLAYER_INV_SLOT) == cosmeticItem
    }


}