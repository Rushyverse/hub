package fr.rushy.hub.inventories

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class ParametersMenu(val player: Player) : Inventory(InventoryType.CHEST_1_ROW, "Paramètres utilisateur") {

    init {
        setItemStack(
            0, createParameterItem(
                Material.WRITABLE_BOOK,
                "Recevoir les messages du chat",
                "Activez ou désactivez les messages reçus dans le chat"
            )
        )

        setItemStack(
            1, createParameterItem(
                Material.CLOCK,
                "Visibilité des joueurs",
                "Configurez les visibilité des joueurs"
            )
        )
    }

    fun createParameterItem(material: Material, name: String, description: String): ItemStack {
        return ItemStack.of(material, 1)
            .withDisplayName(Component.text(name))
            .withLore(
                List(1) { Component.text(description) }
            )
    }
}