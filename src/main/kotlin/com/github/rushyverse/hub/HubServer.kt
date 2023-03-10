package com.github.rushyverse.hub

import com.github.rushyverse.api.RushyServer
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.core.cache.CacheClient
import com.github.rushyverse.core.data.*
import com.github.rushyverse.core.supplier.database.DatabaseSupplierServices
import com.github.rushyverse.core.supplier.database.IDatabaseEntitySupplier
import com.github.rushyverse.core.supplier.http.HttpSupplierServices
import com.github.rushyverse.core.supplier.http.IHttpEntitySupplier
import com.github.rushyverse.hub.configuration.HubConfiguration
import com.github.rushyverse.hub.database.friends.FriendServiceDatabase
import com.github.rushyverse.hub.items.hotbar.HotbarItemsManager
import com.github.rushyverse.hub.listener.PlayerLoginListener
import com.github.rushyverse.hub.listener.PlayerMoveListener
import com.github.rushyverse.hub.listener.PlayerSpawnListener
import com.github.rushyverse.hub.listener.PlayerStartFlyingListener
import com.github.rushyverse.hub.listener.block.PlayerBreakBlockListener
import com.github.rushyverse.hub.listener.block.PlayerPlaceBlockListener
import com.github.rushyverse.hub.listener.item.PlayerDropItemListener
import com.github.rushyverse.hub.listener.item.PlayerInventoryClickListener
import com.github.rushyverse.hub.listener.item.PlayerItemClickListener
import com.github.rushyverse.hub.listener.item.PlayerSwapItemListener
import io.github.universeproject.kotlinmojangapi.MojangAPIImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.lettuce.core.RedisURI
import kotlinx.serialization.json.Json
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceContainer

suspend fun main(args: Array<String>) {
    HubServer(args.firstOrNull()).start()
}

class HubServer(private val configuration: String? = null) : RushyServer() {

    companion object {
        const val BUNDLE_HUB = "hub"
    }

    lateinit var friendService: FriendService private set
    lateinit var mojangService: MojangService private set

    override suspend fun start() {
        start<HubConfiguration>(configuration) {
            val translationsProvider = createTranslationsProvider(
                listOf(
                    API.BUNDLE_API,
                    BUNDLE_HUB
                )
            )

            // Database
            val cacheClient = createCacheClient()
            val databaseSupplierServices = DatabaseSupplierServices(
                FriendCacheService(cacheClient) to FriendServiceDatabase(),
            )
            val httpSupplierServices = HttpSupplierServices(
                MojangAPIImpl(createHttpClient()),
                cacheClient
            )
            friendService =
                FriendService(IDatabaseEntitySupplier.cacheWithCachingDatabaseFallback(databaseSupplierServices))
            mojangService =
                MojangService(IHttpEntitySupplier.cacheWithCachingRestFallback(httpSupplierServices))

            API.registerCommands()
            addCommands()

            val spawnPoint = area.spawnPoint
            val limitY = area.limitY

            val globalEventHandler = MinecraftServer.getGlobalEventHandler()
            addListeners(globalEventHandler, it, translationsProvider, spawnPoint, limitY)

            MinecraftServer.setBrandName("Rushyverse")


        }
    }

    suspend fun createCacheClient(): CacheClient {
        return CacheClient {
            uri = RedisURI.create("redis://default:redispw@localhost:49153")
        }
    }

    fun createHttpClient(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    /**
     * Register all listeners of the server.
     * @param globalEventHandler Event handler of the server.
     * @param instanceContainer Instance container of the server.
     */
    private fun addListeners(
        globalEventHandler: GlobalEventHandler,
        instanceContainer: InstanceContainer, translationsProvider: TranslationsProvider,
        spawnPoint:Pos,
        limitY: Double
    ) {
        globalEventHandler.addListener(PlayerStartFlyingListener())
        globalEventHandler.addListener(PlayerLoginListener(instanceContainer))
        globalEventHandler.addListener(
            PlayerSpawnListener(
                translationsProvider,
                HotbarItemsManager(friendService, mojangService, translationsProvider),
                spawnPoint
            )
        )
        globalEventHandler.addListener(PlayerMoveListener(limitY, spawnPoint))
        globalEventHandler.addListener(PlayerItemClickListener())
        globalEventHandler.addListener(PlayerDropItemListener())
        globalEventHandler.addListener(PlayerSwapItemListener())
        globalEventHandler.addListener(PlayerInventoryClickListener())
        globalEventHandler.addListener(PlayerPlaceBlockListener())
        globalEventHandler.addListener(PlayerBreakBlockListener())
    }

    /**
     * Register all commands.
     */
    private fun addCommands() {
        val commandManager = MinecraftServer.getCommandManager()
    }
}