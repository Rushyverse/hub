@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)

package fr.rushy.hub.configuration

import fr.rushy.hub.configuration.Configuration.Companion.getOrCreateConfigurationFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.test.*

class ConfigurationTest {

    @Nested
    inner class GetOrCreateConfigurationFile {

        @TempDir
        lateinit var tmpDirectory: File

        private lateinit var initCurrentDirectory: String

        @BeforeTest
        fun onBefore() {
            initCurrentDirectory = System.getProperty("user.dir")
            System.setProperty("user.dir", tmpDirectory.absolutePath)
        }

        @AfterTest
        fun onAfter() {
            System.setProperty("user.dir", initCurrentDirectory)
        }

        @Nested
        inner class GetExistingConfigurationFile {

            @Test
            fun `should return the given configuration file`() = runTest {
                createConfigFileAndCheckIfFound("test") {
                    getOrCreateConfigurationFile(it.absolutePath)
                }
            }

            @Test
            fun `should return the default config file without edit it`() = runTest {
                createConfigFileAndCheckIfFound(Configuration.DEFAULT_CONFIG_FILE_NAME) {
                    getOrCreateConfigurationFile()
                }
            }

            private inline fun createConfigFileAndCheckIfFound(fileName: String, block: (File) -> File) {
                val configurationFile = File(tmpDirectory, fileName)
                assertTrue { configurationFile.createNewFile() }

                val content = UUID.randomUUID().toString()
                configurationFile.writeText(content)

                val file = block(configurationFile)
                assertEquals(configurationFile, file)
                assertEquals(content, file.readText())
            }
        }

        @Nested
        inner class GetNonExistingConfigurationFile {

            @Test
            fun `should return the given configuration file`() = runTest {
                assertThrows<FileNotFoundException> {
                    getOrCreateConfigurationFile(getRandomFileInTmpDirectory().absolutePath)
                }
            }
        }

        @Nested
        inner class CreateDefaultConfiguration {
            @Test
            fun `should create the configuration file if config file not found in the current directory`() = runTest {
                val configurationFile = getOrCreateConfigurationFile()
                assertTrue { configurationFile.isFile }

                val expectedConfigurationFile = File(tmpDirectory, Configuration.DEFAULT_CONFIG_FILE_NAME)
                assertEquals(expectedConfigurationFile, configurationFile)

                inputStreamOfDefaultConfiguration().bufferedReader().use {
                    assertEquals(it.readText(), configurationFile.readText())
                }
            }
        }

        @Nested
        inner class ReadHoconConfiguration {
            @Test
            fun `should create default configuration and read it`() = runTest {
                val configurationFile = getOrCreateConfigurationFile()

                val configuration = Configuration.readHoconConfigurationFile(configurationFile)
                assertEquals(
                    Configuration(
                        ServerConfiguration(25565, "world")
                    ),
                    configuration
                )
            }

            @Test
            fun `should throw exception if file not found`() = runTest {
                assertThrows<MissingFieldException> {
                    Configuration.readHoconConfigurationFile(getRandomFileInTmpDirectory())
                }
            }

            @Test
            fun `should throw exception if fields missing`() = runTest {
                val file = getRandomFileInTmpDirectory()
                assertTrue { file.createNewFile() }
                file.writeText("server { }")

                assertThrows<MissingFieldException> {
                    Configuration.readHoconConfigurationFile(file)
                }
            }
        }

        private fun getRandomFileInTmpDirectory() = File(tmpDirectory, UUID.randomUUID().toString())
    }

    private fun inputStreamOfDefaultConfiguration() =
        Configuration::class.java.classLoader.getResourceAsStream(Configuration.DEFAULT_CONFIG_FILE_NAME)
            ?: error("Unable to find default configuration file in server resources")
}