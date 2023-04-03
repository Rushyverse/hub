package com.github.rushyverse.hub

import com.github.rushyverse.api.RushyServer
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.configuration.AreaConfiguration
import com.github.rushyverse.hub.configuration.CacheConfiguration
import com.github.rushyverse.hub.configuration.HubConfiguration
import com.github.rushyverse.hub.configuration.ScoreboardConfiguration
import com.github.rushyverse.core.cache.CacheClient
import com.github.rushyverse.hub.inventories.HotbarItems
import com.github.rushyverse.hub.listener.PlayerLoginListener
import com.github.rushyverse.hub.listener.PlayerMoveListener
import com.github.rushyverse.hub.listener.PlayerSpawnListener
import com.github.rushyverse.hub.listener.PlayerStartFlyingListener
import io.lettuce.core.RedisURI
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
                    com.github.rushyverse.hub.HubServer.BUNDLE_HUB
                )
            )

            // Cache client
            val cacheClient = createCacheClient(cache)

            API.registerCommands()

            MinecraftServer.getConnectionManager().setPlayerProvider { uuid, username, connection ->
                HubPlayer(uuid, username, connection).apply {
                    setLocale(SupportedLanguage.ENGLISH)
                }
            }

            val globalEventHandler = MinecraftServer.getGlobalEventHandler()
            addListeners(globalEventHandler, it, translationsProvider, area, scoreboard)
        }
    }

    suspend fun createCacheClient(configuration: CacheConfiguration): CacheClient {
        return CacheClient {
            uri = RedisURI.create(configuration.uri)
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
        globalEventHandler.addListener(
            PlayerSpawnListener(
                translationsProvider,
                area.spawnPoint,
                scoreboard,
                HotbarItems(translationsProvider)
            )
        )
        globalEventHandler.addListener(PlayerMoveListener(area))
        globalEventHandler.addChild(HotbarItems.listeners())
    }

}