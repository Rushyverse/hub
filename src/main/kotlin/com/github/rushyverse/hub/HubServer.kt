package com.github.rushyverse.hub

import com.github.rushyverse.api.RushyServer
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.command.EmoteCommand
import com.github.rushyverse.hub.configuration.HubConfiguration
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

            val globalEventHandler = MinecraftServer.getGlobalEventHandler()
            addListeners(globalEventHandler, it, translationsProvider)

            addCommands()

            MinecraftServer.setBrandName("Rushyverse")
        }
    }

    /**
     * Register all listeners of the server.
     * @param globalEventHandler Event handler of the server.
     * @param instanceContainer Instance container of the server.
     */
    private fun addListeners(
        globalEventHandler: GlobalEventHandler,
        instanceContainer: InstanceContainer, translationsProvider: TranslationsProvider
    ) {
        globalEventHandler.addListener(PlayerStartFlyingListener())
        globalEventHandler.addListener(PlayerLoginListener(instanceContainer))
        globalEventHandler.addListener(PlayerSpawnListener(translationsProvider, HotbarItemsManager(translationsProvider)))
        globalEventHandler.addListener(PlayerMoveListener())
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
        commandManager.register(EmoteCommand())
    }
}