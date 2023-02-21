package com.github.rushyverse.hub.listener

import com.github.rushyverse.api.extension.setItemStack
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.items.hotbar.HotbarItemsManager
import com.github.rushyverse.hub.scoreboard.HubScoreboard
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawnListener(
    private val translationsProvider: TranslationsProvider,
    private val hotbarItemsManager: HotbarItemsManager,
    private val spawnPoint : Pos,
) : EventListener<PlayerSpawnEvent> {

    override fun eventType(): Class<PlayerSpawnEvent> {
        return PlayerSpawnEvent::class.java
    }

    override fun run(event: PlayerSpawnEvent): EventListener.Result {
        val player = event.player

        // Scoreboard

        val scoreboard = HubScoreboard(translationsProvider, player)
        scoreboard.addViewer(player)

        // Teleport and give items
        player.teleport(spawnPoint)

        giveItems(player)

        return EventListener.Result.SUCCESS
    }

    private fun giveItems(player: Player) {
        val inv = player.inventory
        val locale = SupportedLanguage.ENGLISH.locale

        val menu = hotbarItemsManager.createMenuItemWithHandler(locale)
        inv.setItemStack(4, menu.first, handler = menu.second)
        player.setHeldItemSlot(4)

        val stats = hotbarItemsManager.createStatsItemWithHandler(locale, player)
        inv.setItemStack(5, stats.first, handler = stats.second)

        val cosmetics = hotbarItemsManager.createCosmeticsItemWithHandler(locale)
        inv.setItemStack(3, cosmetics.first, handler = cosmetics.second)

        val parameters = hotbarItemsManager.createParametersItemWithHandler(locale)
        inv.setItemStack(20, parameters.first, handler = parameters.second)

        val lang = hotbarItemsManager.createLangItemWithHandler(locale)
        inv.setItemStack(22, lang.first, handler = lang.second)

        val social = hotbarItemsManager.createSocialItemWithHandler(locale)
        inv.setItemStack(24, social.first, handler = social.second)

        val loyalty = hotbarItemsManager.createLoyaltyItemWithHandler(locale)
        inv.setItemStack(26, loyalty.first, handler = loyalty.second)
    }
}