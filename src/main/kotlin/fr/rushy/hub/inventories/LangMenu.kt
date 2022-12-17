package fr.rushy.hub.inventories

import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class LangMenu(val player: Player) : Inventory(InventoryType.CHEST_1_ROW, "Langue") {

    init {
        setItemStack(0, ItemStack.of(Material.WHITE_BANNER, 1))
        setItemStack(1, ItemStack.of(Material.BLACK_BANNER, 1))
        setItemStack(2, ItemStack.of(Material.RED_BANNER, 1))
    }
}