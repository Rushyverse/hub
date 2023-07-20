package com.github.rushyverse.hub

import com.github.rushyverse.hub.commannds.*
import com.github.rushyverse.hub.config.HubConfig
import com.github.rushyverse.hub.extension.ItemStack
import com.github.rushyverse.hub.gui.nav.NavigatorGUI
import com.github.rushyverse.hub.listener.*
import com.github.shynixn.mccoroutine.bukkit.scope
import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.extension.registerListener
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.player.PluginClientEvents
import com.github.rushyverse.api.translation.*
import com.github.rushyverse.hub.client.ClientHub
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.plus
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

        lateinit var translationsProvider: TranslationsProvider
            private set
    }

    override val clientEvents: PluginClientEvents = HubClientEvents(this)

    lateinit var config: HubConfig
        private set

    lateinit var world: World
        private set

    lateinit var navigatorGui: NavigatorGUI
        private set

    override suspend fun onEnableAsync() {
        super.onEnableAsync()

        modulePlugin<Hub>()
        moduleClients()

        saveDefaultConfig()
        config = HubConfig.parse(getConfig())
        world = server.worlds[0]
        translationsProvider = createTranslationsProvider()

        navigatorGui = NavigatorGUI(config.gamesMenu)

        logger.info("Hub config summary")
        logger.info("$config")

        registerCommands()

        registerListener { GUIListener(this, setOf(navigatorGui, *navigatorGui.gamesGUIs.values.toTypedArray())) }
        registerListener { HotbarItemsListener(this) }
        registerListener { UndesirableEventListener(this) }
    }

    private suspend fun registerCommands() {
        HubCommand(this).register()

    }

    override suspend fun onDisableAsync() {
        super.onDisableAsync()
    }

    override suspend fun createTranslationsProvider(): ResourceBundleTranslationsProvider {
        return (super.createTranslationsProvider()).apply {
            registerResourceBundleForSupportedLocales(BUNDLE_HUB, ResourceBundle::getBundle)
        }
    }

    override fun createClient(player: Player): Client {
        return ClientHub(id, player.uniqueId, scope + SupervisorJob(scope.coroutineContext.job))
    }

    private fun sendHotbarItems(inv: PlayerInventory) {
        val hotbarConfig = config.hotbar

        for (item in hotbarConfig.items) {
            inv.setItem(item.hotbarSlot, ItemStack(item))
        }
    }

    fun teleportHub(client: Client) {
        val player = client.requirePlayer()
        player.teleport(world.spawnLocation)
        player.inventory.apply {
            clear()
            heldItemSlot = 4
            sendHotbarItems(this)
        }
        player.gameMode = GameMode.SURVIVAL

        client.fastBoard.updateLines("Welcome to", "the hub !")
    }
}