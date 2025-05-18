package com.github.rushyverse.hub.gui.nav

import com.github.rushyverse.api.extension.ItemStack
import com.github.rushyverse.api.extension.asComponent
import com.github.rushyverse.api.extension.withoutDecorations
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.gui.commons.GUI
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.Locale

class ProfileGUI : GUI("gui.profile.title", 54) {

   // val plugin = Hub()
   // private val languageGUI = LanguageGUI(plugin)

    override suspend fun applyItems(client: Client, inv: Inventory) {
        val locale = client.lang().locale
        inv.setItem(13, createPlayerHeadItem(client as ClientHub))
        inv.setItem(21, createStatsItem(locale))
        inv.setItem(22, createSettingsItem(locale))
        inv.setItem(23, createSelectLangItem(locale))
        inv.setItem(30, createAchivementItem(locale))
        inv.setItem(31, createFriendlistItem(locale))
    }

    override suspend fun onClick(client: Client, item: ItemStack, event: InventoryClickEvent) {

    }


    private fun createPlayerHeadItem(client: ClientHub): ItemStack {
        val player = client.requirePlayer()

        val rank = "Owner"
        val shards = 0
        val friends = 0
        val wonEvents = 0
        val joined = "08/08/2023"

        val playerHead = ItemStack(Material.PLAYER_HEAD).apply {
            itemMeta = (itemMeta as SkullMeta).apply {
                owningPlayer = Bukkit.getOfflinePlayer(client.playerUUID)
                displayName(
                    player.name().withoutDecorations().color(NamedTextColor.LIGHT_PURPLE)
                )
                lore(
                    listOf(
                        "<yellow>Rank<gray>:<yellow> $rank".asComponent().withoutDecorations(),
                        "<aqua>Shards<gray>:<blue> $shards".asComponent().withoutDecorations(),
                        "<green>Friends<gray>:<dark_green> $friends".asComponent().withoutDecorations(),
                        "<yellow>Won Events<gray>:<gold> $wonEvents".asComponent().withoutDecorations(),
                        "<light_purple>Joined<gray>:<dark_red> $joined".asComponent().withoutDecorations(),
                    )
                )
            }
        }
        return playerHead
    }

    private fun createSettingsItem(locale: Locale): ItemStack {
        val settings = ItemStack(Material.REPEATER).apply {
            itemMeta = itemMeta.apply {
                displayName(
                    translator.getComponent(
                        "profile.settings.name", locale, arrayOf()
                    ).withoutDecorations().color(NamedTextColor.GOLD)
                )
                lore(
                    listOf(
                        "Allows you to edit and control".asComponent().withoutDecorations(),
                        "various personal settings.".asComponent().withoutDecorations()
                    )
                )
            }
        }
        return settings
    }

    private fun createStatsItem(locale: Locale): ItemStack {
        val stats = ItemStack(Material.PAPER).apply {
            itemMeta = itemMeta.apply {
                displayName(
                    translator.getComponent(
                        "profile.stats.name", locale, arrayOf("")
                    ).withoutDecorations().color(NamedTextColor.GOLD)
                )
                lore(
                    listOf(
                        "Showcases your stats for each".asComponent().withoutDecorations(),
                        "game and an overview of all.".asComponent().withoutDecorations()
                    )
                )
            }
        }
        return stats
    }

    private fun createSelectLangItem(locale: Locale): ItemStack {
        val selectLang = ItemStack(Material.PLAYER_HEAD).apply {
            itemMeta = (itemMeta as SkullMeta).apply {
                owningPlayer = Bukkit.getOfflinePlayer("xHue")
                displayName(
                    translator.getComponent(
                        "profile.selectlang.name", locale, arrayOf("")
                    ).withoutDecorations().color(NamedTextColor.GOLD)
                )
                lore(
                    listOf(
                        "Select a language".asComponent().withoutDecorations()
                    )
                )
            }
        }
        return selectLang
    }

    private fun createAchivementItem(locale : Locale): ItemStack {
        val achievementItem = ItemStack(Material.EMERALD) {
            itemMeta = itemMeta.apply {
                displayName(
                    translator.getComponent(
                        "profile.achievement.name", locale, arrayOf()
                    ).withoutDecorations().color(NamedTextColor.GOLD)
                )
                lore(
                    listOf(
                        "Take a look at your very good achievements".asComponent().withoutDecorations()
                    )
                )
            }
        }
        return achievementItem
    }

    private fun createFriendlistItem(locale : Locale): ItemStack {
        val friendListItem = ItemStack(Material.TROPICAL_FISH) {
            itemMeta = itemMeta.apply {
                displayName(
                    translator.getComponent(
                        "profile.friendlist.name", locale, arrayOf()
                    ).withoutDecorations().color(NamedTextColor.GOLD)
                )
                lore(
                    listOf(
                        "Your friendlist".asComponent().withoutDecorations()
                    )
                )
            }
        }
        return friendListItem
    }
}