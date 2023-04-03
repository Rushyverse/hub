package com.github.rushyverse.listener

import com.github.rushyverse.HubPlayer
import com.github.rushyverse.HubScoreboard
import com.github.rushyverse.api.extension.sync
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.configuration.ScoreboardConfiguration
import com.github.rushyverse.inventories.HotbarItems
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawnListener(
    private val translationsProvider: TranslationsProvider,
    private val spawnPoint: Pos,
    private val scoreboardConfig: ScoreboardConfiguration,
    private val hotbar: HotbarItems
) : EventListener<PlayerSpawnEvent> {

    override fun eventType(): Class<PlayerSpawnEvent> {
        return PlayerSpawnEvent::class.java
    }

    override fun run(event: PlayerSpawnEvent): EventListener.Result {
        val player = event.player as HubPlayer

        player.sync {
            teleport(spawnPoint)

            HubScoreboard(
                scoreboardConfig,
                translationsProvider,
                this
            ).addViewer(this)

            GlobalScope.launch{ hotbar.giveItems(this@sync) }
        }

        return EventListener.Result.SUCCESS
    }
}