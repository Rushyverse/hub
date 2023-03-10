package com.github.rushyverse.listener

import com.github.rushyverse.HubScoreboard
import com.github.rushyverse.api.extension.sync
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.configuration.ScoreboardConfiguration
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.scoreboard.Sidebar

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

       /* event.player.getAcquirable<Player>().sync { player ->
            val sidebar = Sidebar(Component.text("Sidebar"))
            sidebar.createLine(
                Sidebar.ScoreboardLine(
                    "score",
                    Component.text("test", NamedTextColor.RED),
                    0
                )
            )
            sidebar.addViewer(player)
        }*/

        return EventListener.Result.SUCCESS
    }
}