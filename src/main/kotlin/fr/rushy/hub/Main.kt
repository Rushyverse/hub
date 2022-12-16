package fr.rushy.hub

import fr.rushy.hub.command.GamemodeCommand
import fr.rushy.hub.command.GiveCommand
import fr.rushy.hub.command.KickCommand
import fr.rushy.hub.command.StopCommand
import fr.rushy.hub.listener.PlayerLoginListener
import fr.rushy.hub.listener.PlayerMoveListener
import fr.rushy.hub.listener.PlayerSpawnListener
import fr.rushy.hub.listener.PlayerStartFlyingListener
import fr.rushy.hub.world.StoneGenerator
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.entity.fakeplayer.FakePlayerOption
import java.util.*


// The main class is only for the example.
class Main {

    companion object {

        private const val DEFAULT_PORT = 25565

        @JvmStatic
        fun main(args: Array<String>) {
            val minecraftServer = MinecraftServer.init()
            val instanceManager = MinecraftServer.getInstanceManager()
            val instanceContainer = instanceManager.createInstanceContainer()
            instanceContainer.setGenerator(StoneGenerator())
            createFakePlayer()

            registerCommands()

            val globalEventHandler = MinecraftServer.getGlobalEventHandler()
            globalEventHandler.addListener(PlayerStartFlyingListener())
            globalEventHandler.addListener(PlayerLoginListener(instanceContainer))
            globalEventHandler.addListener(PlayerSpawnListener())
            globalEventHandler.addListener(PlayerMoveListener())

            val port = args.getOrNull(0)?.toIntOrNull() ?: DEFAULT_PORT
            minecraftServer.start("0.0.0.0", port)
        }

        fun createFakePlayer() {
            val option = FakePlayerOption().apply {
                isRegistered = true
                isInTabList = true
            }
            FakePlayer.initPlayer(UUID.randomUUID(), "Test", option) { fakePlayer ->
                fakePlayer.setNoGravity(true)
                fakePlayer.isAutoViewable = true
            }
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