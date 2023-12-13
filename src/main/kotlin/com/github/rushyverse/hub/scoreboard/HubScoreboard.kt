package com.github.rushyverse.hub.scoreboard

import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.ClientManager
import com.github.rushyverse.api.schedule.SchedulerTask
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.core.cache.RedisConnection
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.shynixn.mccoroutine.bukkit.scope
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import kotlin.time.Duration.Companion.milliseconds

object HubScoreboard {

    private val scoreboardEmptyLine = Component.empty()
    private val scoreboardServerAddress = text("www.rushy.space").color(NamedTextColor.YELLOW)

    private lateinit var task: SchedulerTask
    private var running = false
    private lateinit var titleScrolling: ColorTextScroller

    private val translator: Translator by inject(Hub.ID)

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
        val locale = client.lang().locale
        val rank = "<red>OWNER"
        val shards = "<light_purple>0"
        val lobby = "<yellow>0"
        val players = "<green>${Bukkit.getOnlinePlayers().size}"

        val rankLine =
            translator.getComponent("scoreboard.rank", locale, arrayOf(rank))
                .color(NamedTextColor.WHITE)
        val shardsLine =
            translator.getComponent("scoreboard.shards", locale, arrayOf(shards))
                .color(NamedTextColor.WHITE)
        val lobbyLine =
            translator.getComponent("scoreboard.lobby", locale, arrayOf(lobby))
                .color(NamedTextColor.WHITE)
        val playersLine =
            translator.getComponent("scoreboard.players", locale, arrayOf(players))
                .color(NamedTextColor.WHITE)

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