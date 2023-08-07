package com.github.rushyverse.hub.gui.nav

import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.config.game.GameGUIConfig
import com.github.rushyverse.hub.gui.commons.GUI
import com.github.rushyverse.api.game.GameData
import com.github.rushyverse.api.game.GameState
import com.github.rushyverse.api.game.SharedGameData
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.SupportedLanguage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

class GameGUI(
    val config: GameGUIConfig,
    val dataProvider: SharedGameData
) : GUI(config.gameType, 54) {

    init {
        dataProvider.subscribeOnChange {
            super.sync()
        }
    }

    override fun applyItems(client: Client, inv: Inventory) {

        inv.setItem(1, leaderBoardItem())
        inv.setItem(4, proposalItem())
        inv.setItem(7, prestigeItem())

        val startSlot = 19
        dataProvider.games.forEachIndexed { index, data ->
            inv.setItem(startSlot + index, buildGameIcon(data))
        }
    }

    fun leaderBoardItem() = ItemStack(Material.TOTEM_OF_UNDYING)

    fun proposalItem() = ItemStack(Material.BOOK)

    fun prestigeItem() = ItemStack(Material.EMERALD)

    fun buildGameIcon(data: GameData, locale: Locale = SupportedLanguage.ENGLISH.locale) =
        ItemStack(config.icon.type).apply {
            itemMeta = itemMeta.apply {
                val lore = mutableListOf<Component>()
                displayName(text(config.icon.name).append(text(" #${data.id}")))

                lore.add(stateOfGameLine(data.state, locale))
                lore.add(playersInGameLine(data.players, locale))

                lore(lore)

                amount = data.id
            }
        }

    private fun stateOfGameLine(state:GameState, locale: Locale) = text(
        Hub.translator.translate(
            "state.of.game", locale, BUNDLE_HUB, arrayOf((
                Hub.translator.translate(
                    "state.${state.name.lowercase()}",
                    locale,
                    BUNDLE_HUB
                )
            )
        )
    ))

    private fun playersInGameLine(players:Int, locale: Locale) = text(
        Hub.translator.translate(
            "players.in.game", locale, BUNDLE_HUB, arrayOf(
                players
            )
        ), NamedTextColor.GRAY
    )

    override fun onClick(client: Client, item: ItemStack, clickType: ClickType) {

        if (item.type == config.icon.type){
            val gameIndex = item.amount

            client.requirePlayer().performCommand(config.clickGameCommand(gameIndex))
        }
    }
}