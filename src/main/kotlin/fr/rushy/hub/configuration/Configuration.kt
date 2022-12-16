package fr.rushy.hub.configuration

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import java.io.File

@Serializable
data class Configuration(
    val server: ServerConfiguration
) {

    companion object {

        private const val DEFAULT_CONFIG_FILE = "config.conf"

        fun getOrCreateConfigFile(filePath: String? = null): File {
            if (filePath != null) {
                val configFile = File(filePath)
                if (!configFile.exists()) {
                    error("Config file $filePath does not exist")
                }
                return configFile
            }

            return File(DEFAULT_CONFIG_FILE).apply {
                if (exists()) {
                    return this
                }

                if (!createNewFile()) {
                    error("Unable to create configuration file $absolutePath")
                }

                val defaultConfiguration =
                    Configuration::class.java.classLoader.getResourceAsStream(DEFAULT_CONFIG_FILE)
                        ?: error("Unable to find default configuration file")

                defaultConfiguration.use { inputStream ->
                    outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }

        fun readConfigurationFile(configFile: File) = Hocon.decodeFromConfig(
            serializer(),
            ConfigFactory.parseFile(configFile)
        )
    }
}

@SerialName("server")
@Serializable
data class ServerConfiguration(
    val port: Int = 25565,
    val world: String = "world"
)