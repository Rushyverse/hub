@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)

package fr.rushy.hub.configuration

import fr.rushy.api.configuration.Configuration
import fr.rushy.api.configuration.Configuration.Companion.getOrCreateConfigurationFile
import fr.rushy.hub.AbstractTest
import fr.rushy.hub.utils.randomString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConfigurationTest : AbstractTest() {

    @Test
    fun `name of default configuration file is correct`() = runTest {
        assertEquals("server.conf", Configuration.DEFAULT_CONFIG_FILE_NAME)
    }

    @Nested
    inner class GetOrCreateConfigurationFile {

        @Nested
        inner class GetExistingConfigurationFile {

            @Test
            fun `should return the given configuration file`() = runTest {
                createConfigFileAndCheckIfFound(randomString()) {
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
                val configurationFile = fileOfTmpDirectory(fileName)
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
            fun `should throw exception if file not found`() = runTest {
                assertThrows<FileNotFoundException> {
                    getOrCreateConfigurationFile(getRandomFileInTmpDirectory().absolutePath)
                }
            }

            @Test
            fun `should throw exception if file is not a regular file`() = runTest {
                assertThrows<FileNotFoundException> {
                    getOrCreateConfigurationFile(tmpDirectory.absolutePath)
                }
            }
        }

        @Nested
        inner class CreateDefaultConfiguration {
            @Test
            fun `should create the config file if it's not found in the current directory`() = runTest {
                val configurationFile = getOrCreateConfigurationFile()
                assertTrue { configurationFile.isFile }

                val expectedConfigurationFile = fileOfTmpDirectory(Configuration.DEFAULT_CONFIG_FILE_NAME)
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

                val configuration = Configuration.readHoconConfigurationFile<HubConfiguration>(configurationFile)
                assertEquals(expectedDefaultConfiguration, configuration)
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

        private fun getRandomFileInTmpDirectory() = fileOfTmpDirectory(randomString())
    }

    private fun inputStreamOfDefaultConfiguration() =
        Configuration::class.java.classLoader.getResourceAsStream(Configuration.DEFAULT_CONFIG_FILE_NAME)
            ?: error("Unable to find default configuration file in server resources")
}