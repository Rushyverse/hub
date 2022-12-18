package fr.rushy.hub.inventories

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class SocialMenu(val player: Player) : Inventory(InventoryType.CHEST_1_ROW, "Social") {

    init {
        setItemStack(
            0, createItem(
                Material.WRITTEN_BOOK,
                "Amis",
                "Ajoutez, chattez et rejoignez vos amis à travers le serveur"
            )
        )

        setItemStack(
            1, createItem(
                Material.CLOCK,
                "Guilde",
                "Gestion de la guilde"
            )
        )
    }

    fun createItem(material: Material, name: String, description: String): ItemStack {
        return ItemStack.of(material, 1)
            .withDisplayName(Component.text(name))
            .withLore(
                List(1) { Component.text(description) }
            )
    }
}