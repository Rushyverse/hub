package com.github.rushyverse

import com.github.rushyverse.api.RushyServer
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.configuration.AreaConfiguration
import com.github.rushyverse.configuration.HubConfiguration
import com.github.rushyverse.configuration.ScoreboardConfiguration
import com.github.rushyverse.listener.PlayerLoginListener
import com.github.rushyverse.listener.PlayerMoveListener
import com.github.rushyverse.listener.PlayerSpawnListener
import com.github.rushyverse.listener.PlayerStartFlyingListener
import net.minestom.server.MinecraftServer
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceContainer

suspend fun main(args: Array<String>) {
    HubServer(args.firstOrNull()).start()
}

class HubServer(private val configuration: String? = null) : RushyServer() {

    companion object {
        const val BUNDLE_HUB = "hub"
    }

    override suspend fun start() {
        start<HubConfiguration>(configuration) {
            val translationsProvider = createTranslationsProvider(
                listOf(
                    API.BUNDLE_API,
                    BUNDLE_HUB
                )
            )

            API.registerCommands()

            MinecraftServer.getConnectionManager().setPlayerProvider { uuid, username, connection ->
                HubPlayer(
                    uuid, username, connection
                )
            }

            val globalEventHandler = MinecraftServer.getGlobalEventHandler()
            addListeners(globalEventHandler, it, translationsProvider, area, scoreboard)
        }
    }

    /**
     * Register all listeners of the server.
     * @param globalEventHandler Event handler of the server.
     * @param instanceContainer Instance container of the server.
     */
    private fun addListeners(
        globalEventHandler: GlobalEventHandler,
        instanceContainer: InstanceContainer,
        translationsProvider: TranslationsProvider,
        area: AreaConfiguration,
        scoreboard: ScoreboardConfiguration
    ) {
        globalEventHandler.addListener(PlayerStartFlyingListener())
        globalEventHandler.addListener(PlayerLoginListener(instanceContainer))
        globalEventHandler.addListener(PlayerSpawnListener(translationsProvider, area.spawnPoint, scoreboard))
        globalEventHandler.addListener(PlayerMoveListener(area))
    }

}