package com.github.rushyverse.hub.inventories.game

import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.extension.setItemStack
import com.github.rushyverse.api.extension.setPreviousButton
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class GameMenu(
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player,
    private val game: String
) : IMenu {

    override fun build(): Inventory {
        val title = translationsProvider.translate("game_menu_title_of", locale, HubServer.BUNDLE_HUB, arrayOf(game))
        val inventory = Inventory(InventoryType.CHEST_6_ROW, title)

        val submitProposalItem = getSubmitProposalItem()

        inventory.setItemStack(6, submitProposalItem) { player, _, _, _ ->
            player.openBook(SubmitProposalMenu(player, game).get())
        }

        val playNow = getPlayNowItem()
        inventory.setItemStack(4, playNow) { player, _, _, _ ->
            player.sendMessage(
                Component.text(
                    translationsProvider.translate(
                        "added_to_game_queue_message",
                        locale,
                        HubServer.BUNDLE_HUB,
                        arrayOf(game)
                    ), NamedTextColor.GREEN
                )
            )
        }

        val leaderboardItem = getLeaderboardItem()
        inventory.setItemStack(2, leaderboardItem) { player, _, _, _ ->
            player.openInventory(LeaderboardMenu(translationsProvider, locale, player, game).build())
        }

        inventory.setCloseButton(41)
        inventory.setPreviousButton(39, MainMenu(translationsProvider, locale).build())

        return inventory
    }

    private fun getLeaderboardItem(): ItemStack {
        return ItemStack.builder(Material.GOLDEN_SWORD)
            .meta { it.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES) }
            .displayName(
                Component.text(
                    translationsProvider.translate("game_menu_item_leaderboard", locale, HubServer.BUNDLE_HUB),
                    NamedTextColor.GOLD
                ).decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
            )
            .lore(
                Component.text(
                    translationsProvider.translate(
                        "game_menu_item_leaderboard_lore",
                        locale,
                        HubServer.BUNDLE_HUB,
                        arrayOf(game)
                    ), NamedTextColor.GRAY
                )
            )
            .build()
    }

    private fun getPlayNowItem(): ItemStack = ItemStack.builder(Material.ENDER_PEARL)
        .displayName(
            Component.text(
                translationsProvider.translate("game_menu_item_play_now", locale, HubServer.BUNDLE_HUB),
                NamedTextColor.LIGHT_PURPLE
            )
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false)
        )
        .lore(
            Component.text(
                translationsProvider.translate("game_menu_item_play_now_lore", locale, HubServer.BUNDLE_HUB),
                NamedTextColor.GRAY
            )
        )
        .build()

    private fun getSubmitProposalItem(): ItemStack {
        return ItemStack.builder(Material.PAPER)
            .displayName(
                Component.text(
                    translationsProvider.translate(
                        "game_menu_item_submit_proposal", locale, HubServer
                            .BUNDLE_HUB
                    )
                ).color(NamedTextColor.GREEN)
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
            ).lore(
                Component.text(
                    translationsProvider.translate(
                        "game_menu_item_submit_proposal_lore",
                        locale,
                        HubServer.BUNDLE_HUB
                    ), NamedTextColor.GRAY
                )
            )
            .build()
    }
}