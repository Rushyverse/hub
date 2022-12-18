package fr.rushy.hub

import fr.rushy.hub.command.GamemodeCommand
import fr.rushy.hub.command.GiveCommand
import fr.rushy.hub.command.KickCommand
import fr.rushy.hub.command.StopCommand
import fr.rushy.hub.configuration.Configuration
import fr.rushy.hub.listener.PlayerLoginListener
import fr.rushy.hub.listener.PlayerMoveListener
import fr.rushy.hub.listener.PlayerSpawnListener
import fr.rushy.hub.listener.PlayerStartFlyingListener
import fr.rushy.hub.utils.randomString
import io.mockk.mockk
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
    inner class CreateOrGetConfigurationFile {

        @Test
        fun `should create a configuration file if it doesn't exist`() {
            assertThrows<IOException> {
                HubServer.main(emptyArray())
            }
            val configurationFile = fileOfTmpDirectory(Configuration.DEFAULT_CONFIG_FILE_NAME)
            assertTrue { configurationFile.isFile }

            val configuration = Configuration.readHoconConfigurationFile(configurationFile)
            assertEquals(expectedDefaultConfiguration, configuration)
        }

        @Test
        fun `should use the configuration file if exists`() {
            val configurationFile = fileOfTmpDirectory(randomString())
            assertTrue { configurationFile.createNewFile() }

            val configuration = defaultConfigurationOnAvailablePort()
            configurationToHoconFile(configuration, configurationFile)

            val exception = assertThrows<FileSystemException> {
                HubServer.main(arrayOf(configurationFile.absolutePath))
            }
            assertEquals(configuration.server.world, exception.file.name)
        }

    }

    @Nested
    inner class ShouldUseConfiguration {

        @Test
        fun `should use configuration to turn on the server`() {
            val configuration = defaultConfigurationOnAvailablePort()
            val configurationFile = fileOfTmpDirectory(randomString())
            configurationToHoconFile(configuration, configurationFile)

            copyWorldInTmpDirectory(configuration)

            HubServer.main(arrayOf(configurationFile.absolutePath))

            // If no exception is thrown, the world is loaded
            assertTrue { MinecraftServer.isStarted() }

            val server = MinecraftServer.getServer()
            assertEquals(configuration.server.port, server.port)
            assertEquals("0.0.0.0", server.address)
        }
    }

    @Nested
    inner class ListenerLoaded {

        @Test
        fun `should load the listener`() {
            copyWorldInTmpDirectory()
            HubServer.main(emptyArray())

            val eventHandler = MinecraftServer.getGlobalEventHandler()

            sequenceOf(
                PlayerStartFlyingListener(),
                PlayerLoginListener(mockk()),
                PlayerSpawnListener(),
                PlayerMoveListener()
            ).map { it.eventType() }.all { eventHandler.hasListener(it) }
        }
    }

    @Nested
    inner class CommandLoaded {

        @Test
        fun `should load all commands`() {
            copyWorldInTmpDirectory()
            HubServer.main(emptyArray())

            val commandManager = MinecraftServer.getCommandManager()
            assertContentEquals(
                commandManager.commands.asSequence().map { it::class.java }.sortedBy { it.simpleName }.toList(),
                sequenceOf(
                    StopCommand::class.java,
                    KickCommand::class.java,
                    GiveCommand::class.java,
                    GamemodeCommand::class.java
                ).sortedBy { it.simpleName }.toList()
            )
        }
    }
}