package com.github.rushyverse.hub.gui.nav

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

class ProfileGUI : GUI("gui.profile.title", 27) {

    override suspend fun applyItems(client: Client, inv: Inventory) {
        val locale = client.lang().locale
        inv.setItem(12, createStatsItem(locale))
        inv.setItem(13, createPlayerHeadItem(client as ClientHub))
        inv.setItem(14, createSettingsItem(locale))
        inv.setItem(15, createSelectLangItem(locale))
    }

    override suspend fun onClick(client: Client, item: ItemStack, event: InventoryClickEvent) {

    }

    private fun createPlayerHeadItem(client: ClientHub): ItemStack {
        val player = client.requirePlayer()

        val rank = "<yellow>Rank</yellow>"
        val shards = 0
        val friends = 0
        val wonEvents = 0
        val joined = "08/08/2023"

        val playerHead = ItemStack(Material.PLAYER_HEAD).apply {
            itemMeta = (itemMeta as SkullMeta).apply {
                owningPlayer = Bukkit.getOfflinePlayer(client.playerUUID)
                displayName(player.name().withoutDecorations().color(NamedTextColor.GOLD))
                lore(
                    listOf(
                        "$rank: Owner".asComponent().withoutDecorations(),
                        "<aqua>Shards<gray>:<blue> $shards".asComponent().withoutDecorations(),
                        "<green>Friends<gray>:<dark_green> $friends".asComponent().withoutDecorations(),
                        "<yellow>Won Events<gray>:<gold> $wonEvents".asComponent().withoutDecorations(),
                        "<light_purple>Joined<gray>: <dark_purple>$joined".asComponent().withoutDecorations(),
                    )
                )
            }
        }
        return playerHead
    }

    private fun createSettingsItem(locale: Locale): ItemStack {
        val settings = ItemStack(Material.REDSTONE_BLOCK).apply {
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
            itemMeta = itemMeta.apply {
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
}