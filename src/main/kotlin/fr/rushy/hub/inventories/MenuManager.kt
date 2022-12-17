package fr.rushy.hub.inventories

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class MenuManager(val player: Player) {

    // ALT + SHIFT + M : Extract method

    companion object {
        enum class MenuType(val title: String) {
            MAIN("Menu principal"),
            STATISTICS("Menu des statistiques"),
            COSMETICS("Menu des cosmétiques");

            public fun getFromTitle(title: String): MenuType? {
                for (menuType in values()) {
                    if (menuType.title.equals(title, true)) {
                        return menuType
                    }
                }
                return null
            }

            companion object {
                fun getFromTitle(title: Component): MenuType = getFromTitle(title)
            }
        }

    }

    fun getStatsMenu() : Inventory = StatsMenu(player).get()

    fun getMainMenu(): Inventory {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, MenuType.MAIN.title)



        return inventory
    }



    fun getCosmeticsMenu(): Inventory {
        val inventory = Inventory(InventoryType.CHEST_6_ROW, MenuManager.Companion.MenuType.COSMETICS.title)

        return inventory
    }

}