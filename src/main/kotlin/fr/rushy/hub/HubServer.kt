package fr.rushy.hub

import fr.rushy.api.RushyServer
import fr.rushy.api.configuration.Configuration
import fr.rushy.hub.configuration.HubConfiguration
import fr.rushy.hub.listener.PlayerLoginListener
import fr.rushy.hub.listener.PlayerMoveListener
import fr.rushy.hub.listener.PlayerSpawnListener
import fr.rushy.hub.listener.PlayerStartFlyingListener
import mu.KotlinLogging
import net.minestom.server.MinecraftServer
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceContainer

private val logger = KotlinLogging.logger { }

class HubServer {

    companion object : RushyServer() {

        const val BUNDLE_HUB = "hub"

        @JvmStatic
        override fun main(args: Array<String>) {
            start<HubConfiguration> {
                val translationsProvider = createTranslationsProvider(listOf(BUNDLE_HUB))

                registerCommands()

                val globalEventHandler = MinecraftServer.getGlobalEventHandler()
                addListeners(globalEventHandler, it)
            }
        }

        /**
         * Register all listeners of the server.
         * @param globalEventHandler Event handler of the server.
         * @param instanceContainer Instance container of the server.
         */
        private fun addListeners(
            globalEventHandler: GlobalEventHandler,
            instanceContainer: InstanceContainer
        ) {
            globalEventHandler.addListener(PlayerStartFlyingListener())
            globalEventHandler.addListener(PlayerLoginListener(instanceContainer))
            globalEventHandler.addListener(PlayerSpawnListener())
            globalEventHandler.addListener(PlayerMoveListener())
        }
    }
}