package fr.rushy.hub.inventories

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class StatsMenu(val player: Player) {

    fun get(): Inventory {
        val inventory = Inventory(InventoryType.CHEST_1_ROW, MenuManager.Companion.MenuType.STATISTICS.title)

        val stats = arrayOf("Kills", "Deaths", "K/D", "Coins", "Level", "XP", "Rank")

        sequenceOf("RTF", "RushZone", "MineralContest", "PvPBox")
            .associateWith { stats.map(this::createLoreComponentFromStats) }
            .map { (game, stats) -> createItemStackFromGame(game, stats) }
            .forEach(inventory::addItemStack)

        return inventory
    }

    private fun createItemStackFromGame(game: String, stats: List<Component>) =
        ItemStack.of(Material.PAPER)
            .withDisplayName(
                createDisplayNameFromGame(game)
            ).withLore(stats)

    private fun createDisplayNameFromGame(game: String) = Component.text(game)
        .color(NamedTextColor.AQUA)
        .decorate(TextDecoration.BOLD)
        .decoration(TextDecoration.ITALIC, false)

    private fun createLoreComponentFromStats(stat: String) =
        Component.text("$stat: ")
            .color(NamedTextColor.GRAY)
            .append(Component.text("0").color(NamedTextColor.GREEN))
            .decoration(TextDecoration.ITALIC, false)
}