package fr.rushy.hub

import fr.rushy.hub.command.GamemodeCommand
import fr.rushy.hub.command.GiveCommand
import fr.rushy.hub.command.KickCommand
import fr.rushy.hub.command.StopCommand
import fr.rushy.hub.listener.*
import fr.rushy.hub.listener.block.PlayerBreakBlockListener
import fr.rushy.hub.listener.block.PlayerPlaceBlockListener
import fr.rushy.hub.listener.item.PlayerDropItemListener
import fr.rushy.hub.listener.item.PlayerInventoryClickListener
import fr.rushy.hub.listener.item.PlayerItemClickListener
import fr.rushy.hub.listener.item.PlayerSwapItemListener
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
            createFakePlayer("§DFakePlayer", false)

            registerCommands()

            val globalEventHandler = MinecraftServer.getGlobalEventHandler()
            globalEventHandler.addListener(PlayerStartFlyingListener())
            globalEventHandler.addListener(PlayerLoginListener(instanceContainer))
            globalEventHandler.addListener(PlayerSpawnListener())
            globalEventHandler.addListener(PlayerMoveListener())
            globalEventHandler.addListener(PlayerItemClickListener())
            globalEventHandler.addListener(PlayerDropItemListener())
            globalEventHandler.addListener(PlayerSwapItemListener())
            globalEventHandler.addListener(PlayerInventoryClickListener())
            globalEventHandler.addListener(PlayerPlaceBlockListener())
            globalEventHandler.addListener(PlayerBreakBlockListener())

            val port = args.getOrNull(0)?.toIntOrNull() ?: DEFAULT_PORT
            minecraftServer.start("0.0.0.0", port)
        }

        fun createFakePlayer(name:String, inTabList:Boolean) {

            if (name.length > 16) {
                throw IllegalArgumentException("Name must be 16 characters or less")
            }

            val option = FakePlayerOption().apply {
                isRegistered = true
                isInTabList = inTabList
            }
            FakePlayer.initPlayer(UUID.randomUUID(), name, option) { fakePlayer ->
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