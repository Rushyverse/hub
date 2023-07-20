package com.github.rushyverse.hub.gui.nav

import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.extension.ItemStack
import com.github.rushyverse.hub.config.game.GameIconConfig
import com.github.rushyverse.hub.config.game.GamesGUIConfig
import com.github.rushyverse.hub.gui.commons.GUI
import com.github.rushyverse.api.SharedMemory
import com.github.rushyverse.api.game.SharedGameData
import com.github.rushyverse.api.player.Client
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

class NavigatorGUI(
    val config: GamesGUIConfig,
) : GUI("gui.navigator.title", 54) {

    val dataProvider: SharedGameData
    val gamesGUIs = mutableMapOf<String, GameGUI>()

    init {
        val apiMode = config.dataProvider.apiMode

        dataProvider =
            if (apiMode) {
                // Subscribe to any changes on the shared memory system
                SharedMemory.games.apply { subscribeOnChange { sync() } }
            } else {
                TODO("not implemented")
            }

        // Register gui for each registered game type
        config.games.forEach {
            gamesGUIs[it.gameType] = GameGUI(it, dataProvider)
        }
    }

    override fun applyItems(client: Client, inv: Inventory) {
        println("Call applyItems")

        inv.setItem(2, achievementsMenuItem())
        inv.setItem(4, shopMenuItem())
        inv.setItem(6, statsMenuItem())


        config.games.forEach {
            val iconConfig = it.icon
            val gameType = it.gameType

            val games = dataProvider.games(gameType)
            val gameTypeItem = buildGameIcon(
                iconConfig,
                client.locale,
                dataProvider.players(gameType),
                games,
            ).apply { addItemFlags(*ItemFlag.values()) }

            inv.setItem(iconConfig.menuSlot, gameTypeItem)
        }
    }

    override fun onClick(client: Client, item: ItemStack, clickType: ClickType) {
        val gameConfig = config.games.firstOrNull { it.icon.type == item.type }

        if (gameConfig != null) {
            val gameType = gameConfig.gameType
            val games = dataProvider.games(gameType)

            client.send("games="+games)

            if (games <= 1) {
                client.requirePlayer().performCommand(gameConfig.clickGameCommand(1))
            } else {
                gamesGUIs[gameType]?.open(client)
            }

        } else {
            client.send("§7Other item : ${item.i18NDisplayName}")
        }
    }

    private fun buildGameIcon(iconConfig: GameIconConfig, locale: Locale, players: Int, games: Int) =
        ItemStack(
            iconConfig,
            Component.empty(),
            text(
                Hub.translationsProvider.translate(
                    "games.menu.icon.players.info", locale, BUNDLE_HUB,
                    listOf(players)
                ),
                NamedTextColor.GRAY
            ).append {
                if (games > 1) {
                    text(" ").append(
                        text(
                            Hub.translationsProvider.translate(
                                "games.menu.icon.players.info.games", locale, BUNDLE_HUB,
                                listOf(games)
                            ),
                            NamedTextColor.GRAY
                        )
                    )
                } else {
                    Component.empty()
                }
            },
            Component.empty(),
            text(
                Hub.translationsProvider.translate(
                    "games.menu.icon.info.join", locale, BUNDLE_HUB
                ), NamedTextColor.YELLOW
            )
        )


    private fun survivalNavItem() = ItemStack(Material.STONE_PICKAXE).apply {
        itemMeta = itemMeta.apply {
            displayName(text("survival"))
        }
    }


    private fun achievementsMenuItem() = ItemStack(Material.NETHER_STAR).apply {
        itemMeta = itemMeta.apply {
            displayName(text("achievements"))
        }
    }

    private fun shopMenuItem() = ItemStack(Material.EMERALD).apply {
        itemMeta = itemMeta.apply {
            displayName(text("shop"))
        }
    }

    private fun statsMenuItem() = ItemStack(Material.BOOK).apply {
        itemMeta = itemMeta.apply {
            displayName(text("stats"))
        }
    }
}