package com.github.rushyverse.hub.inventories.game

import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.extension.setPreviousButton
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class LeaderboardMenu(
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player,
    private val game: String
) : IMenu {

    override fun build(): Inventory {
        val title =
            translationsProvider.translate("leaderboard_menu_title", locale, HubServer.BUNDLE_HUB, arrayOf(game))
        val inv = Inventory(InventoryType.CHEST_4_ROW, title)

        var pos = 1
        sequenceOf("Bébére", "Gaston", "Marco", "Patoch", "Bernard")
            .forEach { p ->
                inv.addItemStack(buildPlayerItem(p, pos))
                pos++
            }

        inv.setCloseButton(32)
        inv.setPreviousButton(30, GameMenu(translationsProvider, locale, player, game).build())
        return inv
    }

    private fun buildPlayerItem(p: String, pos: Int): ItemStack {
        return ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(
                Component.text(p).append(
                    Component.text(" - $pos")
                        .color(NamedTextColor.GOLD)
                )
            )
            .lore(Component.text("Score: " + (pos * 100)))
            .build()

    }
}
