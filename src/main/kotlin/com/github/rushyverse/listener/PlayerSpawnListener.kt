package com.github.rushyverse.listener

import com.github.rushyverse.HubScoreboard
import com.github.rushyverse.api.extension.sync
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.configuration.ScoreboardConfiguration
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerSpawnEvent

class PlayerSpawnListener(
    private val translationsProvider: TranslationsProvider,
    private val spawnPoint: Pos,
    private val scoreboardConfig: ScoreboardConfiguration
) : EventListener<PlayerSpawnEvent> {

    override fun eventType(): Class<PlayerSpawnEvent> {
        return PlayerSpawnEvent::class.java
    }

    override fun run(event: PlayerSpawnEvent): EventListener.Result {
        val player = event.player

        player.sync {
            teleport(spawnPoint)
            HubScoreboard(
                scoreboardConfig,
                translationsProvider,
                SupportedLanguage.ENGLISH.locale,
                player
            )
            .addViewer(this)
        }

        return EventListener.Result.SUCCESS
    }
}