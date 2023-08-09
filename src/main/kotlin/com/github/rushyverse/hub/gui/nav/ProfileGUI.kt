package com.github.rushyverse.hub.gui.nav

import com.github.rushyverse.api.extension.asComponent
import com.github.rushyverse.api.extension.withoutDecorations
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.gui.commons.GUI
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.Locale

class ProfileGUI : GUI("gui.profile.title", 27) {

    override suspend fun applyItems(client: Client, inv: Inventory) {
        val locale = client.lang().locale
        inv.setItem(12, createStatsItem(locale))
        inv.setItem(13, createPlayerHead(client as ClientHub))
        inv.setItem(14, createSettingsItem(locale))
    }

    override suspend fun onClick(client: Client, item: ItemStack, clickType: ClickType) {

    }

    private fun createPlayerHead(client: ClientHub): ItemStack {
        val player = client.requirePlayer()

        val rank = "<yellow>Rank</yellow>" // mockk - rank is already colored
        val shards = 0
        val friends = 0
        val wonEvents = 0
        val joined = "08/08/2023"

        val head = ItemStack(Material.PLAYER_HEAD).apply {
            itemMeta = (itemMeta as SkullMeta).apply {
                owningPlayer = Bukkit.getOfflinePlayer(client.playerUUID)
                displayName(player.name().withoutDecorations().color(NamedTextColor.GOLD))
                lore(
                    listOf(
                        "$rank: Owner".asComponent(),
                        "<aqua>Shards<gray>:<blue> $shards".asComponent(),
                        "<green>Friends<gray>:<dark_green> $friends".asComponent(),
                        "<yellow>Won Events<gray>:<gold> $wonEvents".asComponent(),
                        "<light_purple>Joined<gray>: <dark_purple>$joined".asComponent(),
                    )
                )
            }
        }
        return head
    }

    private fun createSettingsItem(locale: Locale): ItemStack {
        val cmdBlock = ItemStack(Material.REDSTONE_TORCH).apply {
            itemMeta = itemMeta.apply {
                displayName(
                    translator.getComponent(
                        "profile.settings.name", locale, arrayOf()
                    ).apply { withoutDecorations().color(NamedTextColor.GOLD) }
                )
                lore(
                    listOf(
                        "".asComponent(),
                        "".asComponent()
                    )
                )
            }
        }
        return cmdBlock
    }

    private fun createStatsItem(locale: Locale): ItemStack {
        val paper = ItemStack(Material.PAPER).apply {
            itemMeta = itemMeta.apply {
                displayName(
                    translator.getComponent(
                        "profile.stats.name", locale, arrayOf("")
                    ).withoutDecorations().color(NamedTextColor.GOLD)
                )
                lore(
                    listOf(
                        "".asComponent(),
                        "".asComponent()
                    )
                )
            }
        }
        return paper
    }
}