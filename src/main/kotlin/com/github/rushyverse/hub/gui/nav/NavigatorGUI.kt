package com.github.rushyverse.hub.gui.nav

import com.github.rushyverse.api.extension.BukkitRunnable
import com.github.rushyverse.api.game.GameData
import com.github.rushyverse.api.game.GameState
import com.github.rushyverse.api.game.SharedGameData
import com.github.rushyverse.api.gui.ItemStackIndex
import com.github.rushyverse.api.gui.LocaleGUI
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.config.game.GameIconConfig
import com.github.rushyverse.hub.config.game.GamesGUIConfig
import com.github.rushyverse.hub.extension.ItemStack
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * On va mettre à jour le système du hub et utiliser l'API de tic
 * Pour le NavigatorGUI, est-ce qu'il y aura des info propres au joueur à part sa langue ? e
 * hmm non Ok, on va découvrir son système x)à
 * je re dans 10 min ok :)
 */
class NavigatorGUI(
    plugin: Hub,
    val config: GamesGUIConfig,
) : LocaleGUI(
    plugin
) {

    private val translator: Translator by inject(Hub.ID)
    val dataProvider: SharedGameData by inject<SharedGameData>()
    val gameGuis = mutableListOf<GameGUI>()

    override suspend fun register(): Boolean {

        dataProvider.subscribeOnChange {
            for(locale in Locale.getAvailableLocales()){
                plugin.launch {
                    update(locale, false)
                }
            }
        }

        coroutineScope {
            launch {
                for (gameConfig in config.games) {

                    repeat(gameConfig.games) {
                        dataProvider.saveUpdate(GameData(gameConfig.gameType, it+1, state = GameState.NOT_STARTED, permanent = true))
                    }

                    gameGuis.add(
                        GameGUI(plugin as Hub, gameConfig).apply { register() }
                    )
                }
            }
        }

        return super.register()
    }

    override suspend fun createInventory(locale: Locale) = Bukkit.createInventory(
        null, 54,
        translator.getComponent("gui.navigator.title", locale, Hub.BUNDLE_HUB)
    )

    override fun getItems(locale: Locale, size: Int): Flow<ItemStackIndex> {
        return flow {
            emit(2 to achievementsMenuItem())
            emit(4 to shopMenuItem())
            emit(6 to statsMenuItem())
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

                emit(iconConfig.menuSlot to gameTypeItem)
            }
        }
    }

    override suspend fun onClick(
        client: Client,
        clickedInventory: Inventory,
        clickedItem: ItemStack,
        event: InventoryClickEvent
    ) {
        val gameConfig = config.games.firstOrNull { it.icon.type == clickedItem.type }
        if (gameConfig != null) {
            val gameType = gameConfig.gameType
            val games = dataProvider.games(gameType)

            if (games <= 1) {
                if (games == 0) {
                    client.send(
                        translator.getComponent("game.created.progress", client.lang().locale)
                    )

                    Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        gameConfig.createGameCommand(1)
                    )

                    BukkitRunnable {
                        client.requirePlayer().performCommand(gameConfig.joinGameCommand(1))
                    }.runTaskLater(plugin, 20L)
                } else
                    client.requirePlayer().performCommand(gameConfig.joinGameCommand(1))
            } else {
                val gameGui = gameGuis.first { it.config == gameConfig }
                gameGui.openClient(client)
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
                        Component.text(" ").append(
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
                "games.menu.icon.info.join", locale, Hub.BUNDLE_HUB
            ).color(NamedTextColor.YELLOW),
            translator = translator
        )

    private fun achievementsMenuItem() = ItemStack(Material.NETHER_STAR).apply {
        itemMeta = itemMeta.apply {
            displayName(Component.text("Achievements"))
        }
    }

    private fun shopMenuItem() = ItemStack(Material.EMERALD).apply {
        itemMeta = itemMeta.apply {
            displayName(Component.text("Shop"))
        }
    }

    private fun statsMenuItem() = ItemStack(Material.BOOK).apply {
        itemMeta = itemMeta.apply {
            displayName(Component.text("Stats"))
        }
    }
}