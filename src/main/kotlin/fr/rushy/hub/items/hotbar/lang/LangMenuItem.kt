package fr.rushy.hub.items.hotbar.lang

import fr.rushy.hub.inventories.LangMenu
import fr.rushy.hub.items.CustomItem
import net.minestom.server.entity.Player
import net.minestom.server.item.Material

class LangMenuItem(val player: Player) : CustomItem(material, "Langue", "Choisissez votre langue") {

    companion object {
        val material = Material.WHITE_BANNER
    }

    override fun onClick(player: Player) {
        player.openInventory(LangMenu(player))
    }

}