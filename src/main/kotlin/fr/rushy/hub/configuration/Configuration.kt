package fr.rushy.hub.configuration

import com.typesafe.config.ConfigFactory
import fr.rushy.hub.utils.workingDirectory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import java.io.File
import java.io.FileNotFoundException

/**
 * Configuration of the server.
 * @property server Configuration about the minestom server.
 */
@Serializable
data class Configuration(
    val server: ServerConfiguration
) {

    companion object {

        /**
         * Default name of the config file.
         * This name is used to create the default config file when the user does not provide one.
         */
        const val DEFAULT_CONFIG_FILE_NAME = "server.conf"

        /**
         * Get the configuration file from the given path.
         * If the path is null, the default config file will be used.
         * If the default config file does not exist, it will be created with the default configuration from resources folder.
         * @param filePath Path of the configuration file.
         * @return The configuration file that must be used to load application configuration.
         */
        fun getOrCreateConfigurationFile(filePath: String? = null): File {
            if (filePath != null) {
                val configFile = File(filePath)
                if (!configFile.isFile) {
                    throw FileNotFoundException("Config file $filePath does not exist or is not a regular file")
                }
                return configFile
            }

            return getOrCreateDefaultConfigurationFile(workingDirectory)
        }

        /**
         * Search for the default config file in the current directory.
         * If the file does not exist, it will be created with the default configuration from resources folder.
         * @return The default config file.
         */
        private fun getOrCreateDefaultConfigurationFile(parent: File): File = File(parent, DEFAULT_CONFIG_FILE_NAME).apply {
            if (exists()) {
                return this
            }

            val defaultConfiguration =
                Configuration::class.java.classLoader.getResourceAsStream(DEFAULT_CONFIG_FILE_NAME)
                    ?: error("Unable to find default configuration file in server resources")

            defaultConfiguration.use { inputStream ->
                if (!createNewFile()) {
                    throw FileSystemException(this, null, "Unable to create configuration file $absolutePath")
                }

                outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        /**
         * Load the configuration from the given file with HOCON format.
         * @param configFile Configuration file to load.
         * @return The configuration loaded from the given file.
         */
        fun readHoconConfigurationFile(configFile: File): Configuration = Hocon.decodeFromConfig(
            serializer(),
            ConfigFactory.parseFile(configFile)
        )
    }
}

/**
 * Configuration about the minestom server.
 * @property port Port of the server.
 * @property world Path of the world to load.
 */
@SerialName("server")
@Serializable
data class ServerConfiguration(
    val port: Int,
    val world: String
)