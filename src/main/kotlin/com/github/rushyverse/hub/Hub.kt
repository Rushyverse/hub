package com.github.rushyverse.hub

import com.charleskorn.kaml.Yaml
import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.configuration.reader.IFileReader
import com.github.rushyverse.api.configuration.reader.YamlFileReader
import com.github.rushyverse.api.configuration.reader.readConfigurationFile
import com.github.rushyverse.api.extension.registerListener
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.serializer.LocationSerializer
import com.github.rushyverse.api.translation.*
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.commannds.*
import com.github.rushyverse.hub.config.HubConfig
import com.github.rushyverse.hub.extension.ItemStack
import com.github.rushyverse.hub.gui.LanguageGUI
import com.github.rushyverse.hub.gui.nav.NavigatorGUI
import com.github.rushyverse.hub.listener.*
import com.github.rushyverse.hub.scoreboard.HubScoreboard
import com.github.shynixn.mccoroutine.bukkit.scope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.plus
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import java.util.*


class Hub(
    override val id: String = "HubPlugin"
) : Plugin() {

    companion object {
        const val BUNDLE_HUB = "hub_translate"

        lateinit var translationsProvider: TranslationProvider
            private set
    }

    lateinit var config: HubConfig private set

    lateinit var world: World private set

    lateinit var navigatorGui: NavigatorGUI private set
    lateinit var langGui: LanguageGUI private set

    override suspend fun onEnableAsync() {
        super.onEnableAsync()

        modulePlugin<Hub>()
        moduleClients()

        val configReader = createYamlReader()
        config = configReader.readConfigurationFile<HubConfig>("config.yml")

        world = server.worlds[0]
        translationsProvider = createTranslationProvider()

        navigatorGui = NavigatorGUI(config.gamesGUI)
        langGui = LanguageGUI()

        logger.info("Hub config summary")
        logger.info("$config")

        registerCommands()

        registerListener { AuthenticationListener(this) }
        registerListener {
            GUIListener(
                this,
                setOf(
                    navigatorGui,
                    *navigatorGui.gamesGUIs.values.toTypedArray(),
                    langGui
                )
            )
        }
        registerListener { HotbarItemsListener(this) }
        registerListener { UndesirableEventListener(this) }

        HubScoreboard.init(this)
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
        LanguagesCommand().register(this)
    }

    override suspend fun onDisableAsync() {
        super.onDisableAsync()
    }

    override suspend fun createTranslationProvider(): ResourceBundleTranslationProvider {
        return (super.createTranslationProvider()).apply {
            registerResourceBundleForSupportedLocales(BUNDLE_HUB, ResourceBundle::getBundle)
        }
    }

    override fun createClient(player: Player): Client {
        return ClientHub(player.uniqueId, scope + SupervisorJob(scope.coroutineContext.job))
    }

    private fun sendHotbarItems(inv: PlayerInventory) {
        val hotbarConfig = config.hotbar

        for (item in hotbarConfig.items) {
            inv.setItem(item.hotbarSlot, ItemStack(item.type, item.name, item.description))
        }
    }

    suspend fun teleportHub(client: ClientHub) {
        val player = client.requirePlayer()
        player.teleport(world.spawnLocation)
        player.inventory.apply {
            clear()
            heldItemSlot = 4
            sendHotbarItems(this)
        }
        player.gameMode = GameMode.SURVIVAL

        HubScoreboard.send(client)
    }
}