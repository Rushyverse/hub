package com.github.rushyverse.hub.gui.nav

import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.extension.ItemStack
import com.github.rushyverse.hub.config.game.GameIconConfig
import com.github.rushyverse.hub.config.game.GamesGUIConfig
import com.github.rushyverse.hub.gui.commons.GUI
import com.github.rushyverse.api.game.SharedGameData
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.getComponent
import com.github.shynixn.mccoroutine.bukkit.SuspendingPlugin
import com.github.shynixn.mccoroutine.bukkit.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

class NavigatorGUI(
    val config: GamesGUIConfig,
) : GUI("gui.navigator.title", 54) {

    val dataProvider: SharedGameData by inject()
    val gamesGUIs = mutableMapOf<String, GameGUI>()

    companion object {

        suspend inline fun of(plugin: SuspendingPlugin, config: GamesGUIConfig): NavigatorGUI {
            val gui = NavigatorGUI(config)
            // Register gui for each registered game type
            config.games.forEach {
                gui.gamesGUIs[it.gameType] = GameGUI.of(plugin, it)
            }

            gui.dataProvider.subscribeOnChange {
                plugin.launch {
                    gui.sync()
                }

            }
            return gui
        }

    }

    override suspend fun applyItems(client: Client, inv: Inventory) {

        val locale = client.lang().locale
        inv.setItem(2, achievementsMenuItem())
        inv.setItem(4, shopMenuItem())
        inv.setItem(6, statsMenuItem())


        config.games.forEach {
            val iconConfig = it.icon
            val gameType = it.gameType
            val games = dataProvider.games(gameType)
            val gameTypeItem = buildGameIcon(
                iconConfig,
                locale,
                dataProvider.players(gameType),
                games,
            ).apply { addItemFlags(*ItemFlag.entries.toTypedArray()) }

            inv.setItem(iconConfig.menuSlot, gameTypeItem)
        }
    }

    override suspend fun onClick(client: Client, item: ItemStack, event: InventoryClickEvent) {
        val gameConfig = config.games.firstOrNull { it.icon.type == item.type }

        if (gameConfig != null) {
            val gameType = gameConfig.gameType
            val games = dataProvider.games(gameType)

            if (games <= 1) {
                client.requirePlayer().performCommand(gameConfig.clickGameCommand(1))
            } else {
                gamesGUIs[gameType]?.open(client)
            }

        }
    }

    private fun buildGameIcon(iconConfig: GameIconConfig, locale: Locale, players: Int, games: Int) =
        ItemStack(
            iconConfig.type,
            iconConfig.name,
            iconConfig.description,
            Component.empty(),
            translator.getComponent(
                "games.menu.icon.players.info", locale,
                arrayOf(players)
            ).color(NamedTextColor.GRAY)
                .append {
                    if (games > 1) {
                        text(" ").append(
                            translator.getComponent(
                                "games.menu.icon.players.info.games", locale,
                                arrayOf(games)
                            ).color(NamedTextColor.GRAY)
                        )
                    } else {
                        Component.empty()
                    }
                },
            Component.empty(),
            translator.getComponent(
                "games.menu.icon.info.join", locale, BUNDLE_HUB
            ).color(NamedTextColor.YELLOW),
            translator = super.translator
        )


    private fun achievementsMenuItem() = ItemStack(Material.NETHER_STAR).apply {
        itemMeta = itemMeta.apply {
            displayName(text("Achievements"))
        }
    }

    private fun shopMenuItem() = ItemStack(Material.EMERALD).apply {
        itemMeta = itemMeta.apply {
            displayName(text("Shop"))
        }
    }

    private fun statsMenuItem() = ItemStack(Material.BOOK).apply {
        itemMeta = itemMeta.apply {
            displayName(text("Stats"))
        }
    }
}