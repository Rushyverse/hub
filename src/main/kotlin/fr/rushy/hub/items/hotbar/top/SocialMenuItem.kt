package fr.rushy.hub.items.hotbar.top

import fr.rushy.hub.inventories.social.SocialMenu
import fr.rushy.hub.items.CustomItem
import net.minestom.server.entity.Player
import net.minestom.server.item.Material

class SocialMenuItem : CustomItem(Material.PAINTING, "§DSocial", "§7Gérer votre profil social") {

    override fun onClick(player: Player) {
        player.openInventory(SocialMenu(player))
    }

}