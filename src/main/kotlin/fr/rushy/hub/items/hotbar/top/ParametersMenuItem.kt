package fr.rushy.hub.items.hotbar.top

import fr.rushy.hub.inventories.ParametersMenu
import fr.rushy.hub.items.CustomItem
import net.minestom.server.entity.Player
import net.minestom.server.item.Material

class ParametersMenuItem : CustomItem(Material.REPEATER, "§EParamètres", "§7Configuez vos paramètres utilisateur") {

    override fun onClick(player: Player) {
        player.openInventory(ParametersMenu(player))
    }

}