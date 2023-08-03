package com.github.rushyverse.hub.scoreboard

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.api.schedule.SchedulerTask
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.shynixn.mccoroutine.bukkit.scope
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import kotlin.time.Duration.Companion.milliseconds

object HubScoreboard {

    private val scoreboardEmptyLine = Component.empty()
    private val scoreboardServerAddress = text("www.rushy.space", TextColor.color(153, 66, 152))

    private lateinit var task: SchedulerTask
    private var running = false
    private lateinit var titleScrolling: ColorTextScroller

    fun init(plugin: Hub) {
        val clients: ClientManager by inject(plugin.id)

        titleScrolling = ColorTextScroller(
            "RUSHYVERSE",
            titleColor = NamedTextColor.LIGHT_PURPLE,
            scrollColor = NamedTextColor.DARK_PURPLE,
            bold = true,
            ScrollTextType.WAVE_RIGHT,
            ScrollTextType.WAVE_LEFT,
            ScrollTextType.RANDOM,
            ScrollTextType.FLASH,
        )

        task = SchedulerTask(
            plugin.scope,
            50.milliseconds,
            stopWhenNoTask = true
        ).apply {
            addUnsafe {
                val newTitle = titleScrolling.next()
                val hubPlayers = plugin.world.players

                if (hubPlayers.size == 0) {
                    task.cancel()
                    HubScoreboard.running = false
                } else {
                    hubPlayers.forEach {
                        clients.getClient(it)?.scoreboard()?.updateTitle(newTitle)
                    }
                }
            }
        }
    }

    suspend fun send(client: ClientHub) {
        val rank = "§COWNER"
        val shards = "§D0"
        val lobby = "§E0"
        val players =
            "§A${Bukkit.getOnlinePlayers().size}"
        val locale = client.lang.locale

        val rankLine = text(
            Hub.translationsProvider.translate("scoreboard.rank", locale, Hub.BUNDLE_HUB, listOf(rank))
        ).color(NamedTextColor.GRAY)

        val shardsLine = text(
            Hub.translationsProvider.translate("scoreboard.shards", locale, Hub.BUNDLE_HUB, listOf(shards))
        ).color(NamedTextColor.GRAY)

        val lobbyLine = text(
            Hub.translationsProvider.translate("scoreboard.lobby", locale, Hub.BUNDLE_HUB, listOf(lobby))
        ).color(NamedTextColor.GRAY)

        val playersLine = text(
            Hub.translationsProvider.translate("scoreboard.players", locale, Hub.BUNDLE_HUB, listOf(players))
        ).color(NamedTextColor.GRAY)

        client.scoreboard().apply {
            updateLines(
                scoreboardEmptyLine,
                rankLine,
                shardsLine,
                scoreboardEmptyLine,
                lobbyLine,
                playersLine,
                scoreboardEmptyLine,
                scoreboardServerAddress
            )
        }

        if (!running) {
            println("Starting the task")
            task.start()
            running = true
        }
    }


}