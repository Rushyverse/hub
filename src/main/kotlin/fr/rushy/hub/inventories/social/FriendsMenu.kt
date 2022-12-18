package fr.rushy.hub.inventories.social

import fr.rushy.invext.setBackButton
import fr.rushy.invext.setCloseButton
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class FriendsMenu(val player: Player) : Inventory(InventoryType.CHEST_4_ROW, "Amis") {

    init {
        setItemStack(
            1, createItem(
                Material.DIRT,
                "Ami #1",
                "§CHors ligne"
            )
        )

        setBackButton(30, SocialMenu(player))
        setCloseButton(32)
    }

    fun createItem(material: Material, name: String, description: String): ItemStack {
        return ItemStack.of(material, 1)
            .withDisplayName(Component.text(name))
            .withLore(
                List(1) { Component.text(description) }
            )
    }
}