package fr.rushy.hub

import fr.rushy.hub.command.GamemodeCommand
import fr.rushy.hub.command.GiveCommand
import fr.rushy.hub.command.KickCommand
import fr.rushy.hub.command.StopCommand
import fr.rushy.hub.configuration.Configuration
import fr.rushy.hub.configuration.ServerConfiguration
import fr.rushy.hub.listener.PlayerLoginListener
import fr.rushy.hub.listener.PlayerMoveListener
import fr.rushy.hub.listener.PlayerSpawnListener
import fr.rushy.hub.listener.PlayerStartFlyingListener
import mu.KotlinLogging
import net.minestom.server.MinecraftServer
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.InstanceContainer
import java.io.File

private val logger = KotlinLogging.logger { }

class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val config = loadConfiguration(args.firstOrNull())
            val serverConfig = config.server

            val minecraftServer = MinecraftServer.init()
            val instanceManager = MinecraftServer.getInstanceManager()
            val instanceContainer = instanceManager.createInstanceContainer()

            loadWorld(serverConfig, instanceContainer)

            registerCommands()

            val globalEventHandler = MinecraftServer.getGlobalEventHandler()
            addListeners(globalEventHandler, instanceContainer)

            minecraftServer.start("0.0.0.0", serverConfig.port)
        }

        private fun loadWorld(
            serverConfig: ServerConfiguration,
            instanceContainer: InstanceContainer
        ) {
            val anvilWorld = File(serverConfig.world)
            if (!anvilWorld.exists()) {
                error("World ${anvilWorld.absolutePath} does not exist")
            }

            logger.info { "Loading world ${anvilWorld.absolutePath}" }
            instanceContainer.chunkLoader = AnvilLoader(anvilWorld.toPath())
        }

        private fun loadConfiguration(configFile: String?): Configuration {
            val configurationFile = Configuration.getOrCreateConfigFile(configFile)
            logger.info { "Loading configuration from $configurationFile" }
            val config = Configuration.readConfigurationFile(configurationFile)
            logger.info { "Configuration loaded" }
            return config
        }

        private fun addListeners(
            globalEventHandler: GlobalEventHandler,
            instanceContainer: InstanceContainer
        ) {
            globalEventHandler.addListener(PlayerStartFlyingListener())
            globalEventHandler.addListener(PlayerLoginListener(instanceContainer))
            globalEventHandler.addListener(PlayerSpawnListener())
            globalEventHandler.addListener(PlayerMoveListener())
        }

        /**
         * Register all commands.
         */
        private fun registerCommands() {
            val commandManager = MinecraftServer.getCommandManager()
            commandManager.register(StopCommand())
            commandManager.register(KickCommand())
            commandManager.register(GiveCommand())
            commandManager.register(GamemodeCommand())
        }
    }
}