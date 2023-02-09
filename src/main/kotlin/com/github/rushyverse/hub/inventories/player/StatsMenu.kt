package com.github.rushyverse.hub.inventories.player

import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class StatsMenu(
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player
) : IMenu {

    override suspend fun build(): Inventory {
        val title = translationsProvider.translate("stats_menu_title", locale, HubServer.BUNDLE_HUB)
        val inventory = Inventory(InventoryType.CHEST_1_ROW, title)

        val stats = arrayOf("Kills", "Deaths", "K/D", "Coins", "Level", "XP", "Rank")

        sequenceOf("RTF", "RushZone", "MineralContest", "PvPBox")
            .associateWith { stats.map(this::createLoreComponentFromStats) }
            .map { (game, stats) -> createItemStackFromGame(game, stats) }
            .forEach(inventory::addItemStack)

        return inventory
    }

    private fun createItemStackFromGame(game: String, stats: List<Component>) =
        ItemStack.builder(Material.PAPER)
            .displayName(createDisplayNameFromGame(game))
            .lore(stats)
            .build()

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