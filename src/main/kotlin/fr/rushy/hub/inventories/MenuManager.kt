package fr.rushy.hub.inventories

import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType

class MenuManager {
    fun getMainMenu(player: Player): Inventory {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, "Menu principal")

        return inventory
    }

    fun getStatsMenu(player:Player): Inventory {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, "Menu des statistiques")

        return inventory
    }

    fun getCosmeticsMenu(player:Player): Inventory {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, "Menu des cosmétiques")

        return inventory
    }

}