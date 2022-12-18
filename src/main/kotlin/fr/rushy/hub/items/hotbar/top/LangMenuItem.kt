package fr.rushy.hub.items.hotbar.top

import fr.rushy.hub.inventories.LangMenu
import fr.rushy.hub.items.CustomItem
import net.minestom.server.entity.Player
import net.minestom.server.item.Material

class LangMenuItem : CustomItem(Material.WHITE_BANNER, "§DLangue", "§7Choisissez votre langue") {

    override fun onClick(player: Player) {
        player.openInventory(LangMenu(player))
    }

}