package com.github.rushyverse.hub

import com.charleskorn.kaml.Yaml
import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.configuration.reader.IFileReader
import com.github.rushyverse.api.configuration.reader.YamlFileReader
import com.github.rushyverse.api.configuration.reader.readConfigurationFile
import com.github.rushyverse.api.extension.registerListener
import com.github.rushyverse.api.gui.GUIManager
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.serializer.LocationSerializer
import com.github.rushyverse.api.translation.*
import com.github.rushyverse.core.cache.CacheClient
import com.github.rushyverse.core.data.*
import com.github.rushyverse.core.data.player.*
import com.github.rushyverse.core.supplier.database.DatabaseSupplierConfiguration
import com.github.rushyverse.core.supplier.database.IDatabaseEntitySupplier
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.commands.*
import com.github.rushyverse.hub.commands.staffonly.ScrambleCommand
import com.github.rushyverse.hub.config.HubConfig
import com.github.rushyverse.hub.extension.ItemStack
import com.github.rushyverse.hub.gui.LanguageGUI
import com.github.rushyverse.hub.gui.collectible.ShopGUI
import com.github.rushyverse.hub.gui.nav.NavigatorGUI
import com.github.rushyverse.hub.gui.nav.ProfileGUI
import com.github.rushyverse.hub.listener.*
import com.github.rushyverse.hub.scoreboard.HubScoreboard
import com.github.rushyverse.hub.tab.ActionBar
import com.github.rushyverse.hub.tab.Antiswear
import com.github.rushyverse.hub.tab.ListPLayers
import com.github.shynixn.mccoroutine.bukkit.scope
import dev.jorel.commandapi.CommandAPI
import io.lettuce.core.RedisURI
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.Option
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.await
import kotlinx.coroutines.job
import kotlinx.coroutines.plus
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta
import org.komapper.dialect.postgresql.PostgreSqlDialect
import org.komapper.r2dbc.R2dbcDatabase
import java.util.*

class Hub : Plugin(ID, BUNDLE_HUB) {

    companion object {
        const val BUNDLE_HUB = "hub_translate"
        const val ID = "HubPlugin"
    }

    lateinit var config: HubConfig private set
    lateinit var world: World private set

    lateinit var guiManager: GUIManager private set
    lateinit var navigatorGui: NavigatorGUI private set
    lateinit var profileGui: ProfileGUI private set
    lateinit var shopGui: ShopGUI private set
    lateinit var languageGui: LanguageGUI private set

    lateinit var cacheClient: CacheClient private set

    lateinit var friendService: FriendService private set
    lateinit var guildService: GuildService private set
    lateinit var playerService: PlayerService private set

    override suspend fun onEnableAsync() {
        super.onEnableAsync()

        CommandAPI.unregister("msg")

        modulePlugin<Hub>()
        moduleClients()

        cacheClient = createCacheClient()
        loadDatabase(cacheClient)

        val configReader = createYamlReader()
        config = configReader.readConfigurationFile<HubConfig>("config.yml")

        world = server.worlds.first()

        navigatorGui = NavigatorGUI(this, config.gamesGUI).apply { register() }
        profileGui = ProfileGUI()
        shopGui = ShopGUI(this, translator).apply { register() }
        languageGui = LanguageGUI(this)


        logger.info("Hub config summary")
        logger.info("$config")

        registerCommands()

        registerListener { AuthenticationListener(this) }
        registerListener { HotbarItemsListener(this) }
        registerListener { UndesirableEventListener(this) }
        registerListener { CosmeticListener(this) }

        registerListener { ListenerGUI() }
        registerListener { ListPLayers() }
        registerListener { Antiswear() }
        registerListener { ActionBar(this) }

        HubScoreboard.init(this)
    }

    override suspend fun onDisableAsync() {
        cacheClient.closeAsync().await()
        super.onDisableAsync()
    }

    suspend fun createCacheClient(): CacheClient {
        return CacheClient {
            uri = RedisURI.create("redis://localhost:6379/0")
        }
    }

    fun loadDatabase(cacheClient: CacheClient) {
        val r2dbc = R2dbcDatabase(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, PostgreSqlDialect.driver)
                .option(ConnectionFactoryOptions.USER, "postgres")
                .option(ConnectionFactoryOptions.PASSWORD, "password")
                .option(ConnectionFactoryOptions.HOST, "localhost")
                .option(ConnectionFactoryOptions.PORT, 5432)
                .option(ConnectionFactoryOptions.DATABASE, "postgres")
                .option(Option.valueOf("DB_CLOSE_DELAY"), "-1")
                .build()
        )

        val databaseSupplierConf = DatabaseSupplierConfiguration(
            FriendCacheService(cacheClient) to FriendDatabaseService(r2dbc),
            GuildCacheService(cacheClient) to GuildDatabaseService(r2dbc),
            PlayerCacheService(cacheClient) to PlayerDatabaseService(r2dbc)
        )

        friendService = FriendService(IDatabaseEntitySupplier.cacheWithCachingDatabaseFallback(databaseSupplierConf))
        guildService = GuildService(IDatabaseEntitySupplier.cacheWithCachingDatabaseFallback(databaseSupplierConf))
        playerService = PlayerService(IDatabaseEntitySupplier.cacheWithCachingDatabaseFallback(databaseSupplierConf))
    }

    /**
     * Create a new instance of yaml reader.
     * @return The instance of the yaml reader.
     */
    private fun createYamlReader(): IFileReader {
        val yaml = Yaml(
            serializersModule = SerializersModule {
                contextual(LocationSerializer)
            }
        )
        return YamlFileReader(this, yaml)
    }

    private suspend fun registerCommands() {
        HubCommand(this).register()
        HelpCommand().register()
        LanguagesCommand().register(this)
        VisibilityCommand().register(this)
        NavigatorCommand().register(this)
        ProfileCommand().register(this)
        PingCommand().register()
        MessageCommand().register(this)
        ShopCommand().register(this)

        // staff-only commands
        ScrambleCommand().register()

        DataCommand(this).register()
    }

    override fun createTranslator(): ResourceBundleTranslator {
        return super.createTranslator().apply {
            registerResourceBundleForSupportedLocales(BUNDLE_HUB, ResourceBundle::getBundle)
        }
    }

    override fun createClient(player: Player): Client {
        return ClientHub(player.uniqueId, scope + SupervisorJob(scope.coroutineContext.job))
    }

    fun sendHotbarItems(lang: SupportedLanguage, player: Player) {
        for (item in config.hotbar) {

            val itemStack = ItemStack(
                item.type, item.name, item.description, locale = lang.locale,
                translator = this.translator
            )

            if (item.applySkinHeadOfPlayer) {
                itemStack.itemMeta = (itemStack.itemMeta as SkullMeta).apply {
                    owningPlayer = player
                }
            }

            player.inventory.setItem(item.hotbarSlot, itemStack)
        }
    }

    suspend fun teleportHub(client: ClientHub) {
        val player = client.requirePlayer()
        player.teleport(world.spawnLocation)
        player.inventory.apply {
            clear()
            heldItemSlot = 4
            sendHotbarItems(client.lang(), player)
        }
        player.gameMode = GameMode.SURVIVAL
        player.walkSpeed = 0.2F

        HubScoreboard.send(client)

        updateVisibility(client)
    }

    private suspend fun updateVisibility(client: ClientHub) {
        val player = client.requirePlayer()
        if (client.canSeePlayers) {
            client.showOtherPlayers(this)
        } else {
            client.hideOtherPlayers(this)
        }

        for (otherPlayer in world.players) {
            if (otherPlayer == player) continue
            val otherClient = clientManager.getClient(otherPlayer) as ClientHub

            // Other players can see the player ?
            if (otherClient.canSeePlayers) {
                otherPlayer.showPlayer(this, player)
            } else {
                otherPlayer.hidePlayer(this, player)
            }
        }
    }
}