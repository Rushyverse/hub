package fr.rushy.hub.items.hotbar

import fr.rushy.hub.inventories.MenuManager
import fr.rushy.hub.items.CustomItem
import net.minestom.server.entity.Player
import net.minestom.server.item.Material

class StatsMenuItem : CustomItem(Material.PLAYER_HEAD, "Statistiques") {

    override fun onClick(player: Player) {
        player.openInventory(MenuManager().getStatsMenu(player))
    }
}