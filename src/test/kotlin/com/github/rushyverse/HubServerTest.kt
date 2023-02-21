package com.github.rushyverse

import com.github.rushyverse.api.command.GamemodeCommand
import com.github.rushyverse.api.command.GiveCommand
import com.github.rushyverse.api.command.KickCommand
import com.github.rushyverse.api.command.StopCommand
import com.github.rushyverse.api.configuration.IConfiguration
import com.github.rushyverse.hub.configuration.HubConfiguration
import com.github.rushyverse.hub.HubServer
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
import com.github.rushyverse.utils.randomString
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.minestom.server.MinecraftServer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import kotlin.test.*

class HubServerTest : AbstractTest() {

    @AfterTest
    override fun onAfter() {
        super.onAfter()
        MinecraftServer.stopCleanly()
    }

    @Nested
    inner class CreateOrGetConfiguration {

        @Test
        fun `should create a configuration file if it doesn't exist`() = runTest {
            assertThrows<IOException> {
                HubServer().start()
            }
            val configurationFile = fileOfTmpDirectory(IConfiguration.DEFAULT_CONFIG_FILE_NAME)
            assertTrue { configurationFile.isFile }

            val configuration = IConfiguration.readHoconConfigurationFile<HubConfiguration>(configurationFile)
            assertEquals(expectedDefaultConfiguration, configuration)
        }

        @Test
        fun `should use the configuration file if exists`() = runTest {
            val configurationFile = fileOfTmpDirectory(randomString())
            assertTrue { configurationFile.createNewFile() }

            val configuration = defaultConfigurationOnAvailablePort()
            configurationToHoconFile(configuration, configurationFile)

            val exception = assertThrows<FileSystemException> {
                HubServer(configurationFile.absolutePath).start()
            }
            assertEquals(configuration.server.world, exception.file.name)
        }

    }

    @Nested
    inner class UseConfiguration {

        @Test
        fun `should use configuration to turn on the server`() = runTest {
            val configuration = defaultConfigurationOnAvailablePort()
            val configurationFile = fileOfTmpDirectory(randomString())
            configurationToHoconFile(configuration, configurationFile)

            copyWorldInTmpDirectory(configuration)

            HubServer(configurationFile.absolutePath).start()

            // If no exception is thrown, the world is loaded
            assertTrue { MinecraftServer.isStarted() }

            val server = MinecraftServer.getServer()
            assertEquals(configuration.server.port, server.port)
            assertEquals("0.0.0.0", server.address)
        }
    }

    @Nested
    inner class Listener {

        @Test
        fun `should load the listeners`() = runTest {
            copyWorldInTmpDirectory()
            HubServer().start()

            val eventHandler = MinecraftServer.getGlobalEventHandler()
            val spawnPoint = HubServer.spawnPoint
            val limitY = HubServer.limitY

            sequenceOf(
                PlayerStartFlyingListener(),
                PlayerLoginListener(mockk()),
                PlayerSpawnListener(mockk(), mockk(), spawnPoint),
                PlayerMoveListener(limitY, spawnPoint),
                PlayerItemClickListener(),
                PlayerDropItemListener(),
                PlayerSwapItemListener(),
                PlayerInventoryClickListener(),
                PlayerPlaceBlockListener(),
                PlayerBreakBlockListener()
            ).map { it.eventType() }.all { eventHandler.hasListener(it) }
        }
    }

    @Nested
    inner class Command {

        @Test
        fun `should load all commands`() = runTest {
            // Needed for avoid any crashes, otherwise, CommandManager will be null or empty.
            copyWorldInTmpDirectory()
            HubServer().start()

            val commandManager = MinecraftServer.getCommandManager()
            assertContentEquals(
                commandManager.commands.asSequence().map { it::class.java }.sortedBy { it.simpleName }.toList(),
                sequenceOf(
                    // API COMMANDS registered in HubServer
                    StopCommand::class.java,
                    KickCommand::class.java,
                    GiveCommand::class.java,
                    GamemodeCommand::class.java
                    // HUB COMMANDS
                ).sortedBy { it.simpleName }.toList()
            )
        }
    }
}