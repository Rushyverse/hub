package com.github.rushyverse.hub.gui.nav

import com.github.rushyverse.api.extension.BukkitRunnable
import com.github.rushyverse.api.extension.withItalic
import com.github.rushyverse.api.extension.withoutItalic
import com.github.rushyverse.api.game.GameData
import com.github.rushyverse.api.game.GameState
import com.github.rushyverse.api.game.SharedGameData
import com.github.rushyverse.api.gui.ItemStackIndex
import com.github.rushyverse.api.gui.LocaleGUI
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.config.game.GameGUIConfig
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import javax.inject.Named

class GameGUI(plugin: Hub, val config: GameGUIConfig) : LocaleGUI(plugin) {
    private val translator: Translator by inject(plugin.id)
    private val dataProvider: SharedGameData by inject<SharedGameData>()

    init {
        dataProvider.subscribeOnChange {
            for (locale in Locale.getAvailableLocales()) {
                plugin.launch {
                    update(locale, false)
                }
            }
        }
    }

    override suspend fun createInventory(locale: Locale) = Bukkit.createInventory(
        null, 27, text(config.gameType)
    )

    override fun getItems(key: Locale, size: Int): Flow<ItemStackIndex> {
        return flow {
            val startSlot = 10
            dataProvider.games.forEachIndexed { index, data ->
                emit(startSlot + index to buildGameIcon(data))
            }
        }
    } // Mais du coup comme avant si la game est terminée ça kick tout le monde ça reload la map (le wool est en noir et y a écrit patientez)


    override suspend fun onClick(
        client: Client,
        clickedInventory: Inventory,
        clickedItem: ItemStack,
        event: InventoryClickEvent
    ) {
        if (clickedItem.type == config.icon.type) {
            if (config.games > 0) {
                val gameId = clickedItem.amount
                val game = dataProvider.games.find { it.id == gameId }
                if (game != null && game.state == GameState.NOT_STARTED) {
                    Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        config.createGameCommand(gameId)
                    )
                }
            }
            BukkitRunnable {
                client.requirePlayer().performCommand(config.joinGameCommand(clickedItem.amount))
            }.runTaskLater(plugin, 20L)

        }
    }


    fun buildGameIcon(data: GameData, locale: Locale = SupportedLanguage.ENGLISH.locale) =
        ItemStack(config.icon.type).apply {
            itemMeta = itemMeta.apply {
                val lore = mutableListOf<Component>()
                displayName(text(config.icon.name)
                    .append(text(" #${data.id}"))
                    .withoutItalic()
                    .color(NamedTextColor.LIGHT_PURPLE))

                lore.add(stateOfGameLine(data.state, locale))
                lore.add(playersInGameLine(data.players, locale))

                lore(lore)

                amount = data.id
            }
        }

    private fun stateOfGameLine(state: GameState, locale: Locale) =
        translator.getComponent(
            "state.of.game", locale, arrayOf(
                "${state.miniColor}${translator.get(
                    "state.${state.name.lowercase()}",
                    locale
                )}"
            )
        ).color(NamedTextColor.GRAY).withoutItalic()

    private fun playersInGameLine(players: Int, locale: Locale) =
        translator.getComponent(
            "players.in.game", locale, arrayOf(
                players
            )
        ).color(NamedTextColor.GRAY).withoutItalic()
}