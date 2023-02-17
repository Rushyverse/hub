package com.github.rushyverse.hub.inventories.game

import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.extension.setItemStackSuspend
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class MainMenu(private val translationsProvider: TranslationsProvider, private val locale: Locale) : IMenu {

    override suspend fun build(): Inventory {
        val title = translationsProvider.translate("main_menu_title", locale, HubServer.BUNDLE_HUB)
        val inventory = Inventory(InventoryType.CHEST_6_ROW, title)

        var slot = 11// Starting slot

        val materials =
            listOf(Material.RED_WOOL, Material.SHEARS, Material.DIAMOND, Material.WOODEN_SWORD, Material.BLUE_BED)
        var index = 0
        sequenceOf("RTF", "RushZone", "MineralContest", "PvPBox", "BedWars").forEach {
            val game = it
            val item = buildGameItem(game, materials.get(index))
            inventory.setItemStackSuspend(slot, item) { player, _, _, _ ->
                val menu = GameMenu(translationsProvider, locale, player, game).build()
                player.openInventory(menu)
            }
            slot++
            index++
        }

        inventory.setCloseButton(40)

        return inventory
    }

    private fun buildGameItem(game: String, material: Material): ItemStack {
        return ItemStack.builder(material)
            .displayName(
                Component.text(game)
                    .color(NamedTextColor.AQUA)
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
            )
            .meta { it.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES) }
            .lore(
                Component.text(
                    translationsProvider.translate(
                        "item_lore_click_to_access_game",
                        locale,
                        HubServer.BUNDLE_HUB
                    )
                ).color(NamedTextColor.GRAY),
                Component.empty(),
                Component.text(
                    translationsProvider.translate(
                        "item_lore_players_in_game",
                        locale,
                        HubServer.BUNDLE_HUB,
                        arrayOf(0)
                    )
                ).color(NamedTextColor.GRAY),
            )
            .build()
    }

}