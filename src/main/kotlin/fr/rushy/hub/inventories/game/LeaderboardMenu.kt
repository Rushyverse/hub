package fr.rushy.hub.inventories.game

import fr.rushy.invext.setBackButton
import fr.rushy.invext.setCloseButton
import fr.rushy.invext.withLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class LeaderboardMenu(val player: Player, val game: String) {

    fun get(): Inventory {
        val inv = Inventory(InventoryType.CHEST_4_ROW, "Classement du jeu $game")

        var pos = 1;
        sequenceOf("Bébére", "Gaston", "Marco", "Patoch", "Bernard")
            .forEach { p ->
                inv.addItemStack(buildPlayerItem(p, pos))
                pos++
            }

        inv.setCloseButton(32)
        inv.setBackButton(30, GameMenu(player, game).get())
        return inv;
    }

    private fun buildPlayerItem(p: String, pos:Int): ItemStack {
        return ItemStack.of(Material.PLAYER_HEAD)
            .withDisplayName(Component.text(p).append(Component.text(" - $pos")
                .color(NamedTextColor.GOLD)))
            .withLore("Score: " + (pos * 100))

    }
}
