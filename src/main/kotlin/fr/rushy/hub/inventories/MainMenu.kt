package fr.rushy.hub.inventories

import fr.rushy.hub.inventories.game.GameMenu
import fr.rushy.invext.Clickable
import fr.rushy.invext.setClickableItem
import fr.rushy.invext.withLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class MainMenu(val player: Player) {

    fun get(): Inventory {
        val title = "Menu principal";
        val inventory = Inventory(InventoryType.CHEST_6_ROW, title)

        var slot = 10// Starting slot
        sequenceOf("RTF", "RushZone", "MineralContest", "PvPBox").forEach {
            val game = it;
            val item = buildGameItem(game)
            inventory.setClickableItem(slot, item, Clickable {
                player.openInventory(GameMenu(player, game).get())
            })
            slot++
        }

        return inventory;
    }

    private fun buildGameItem(game:String) :ItemStack{
        val item = ItemStack.of(Material.PAPER)
            .withDisplayName(Component.text("$game")
                .color(NamedTextColor.AQUA)
                .decoration(TextDecoration.BOLD, true))
            .withLore("§7Cliquez pour accéder au menu du jeu\n\n§7Actuellement §D0 §7joueurs")
        return item
    }

}