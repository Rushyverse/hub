package fr.rushy.hub.configuration

import fr.rushy.api.configuration.Configuration
import fr.rushy.api.configuration.ServerConfiguration
import kotlinx.serialization.Serializable

/**
 * Configuration of the server.
 * @property server Configuration about the minestom server.
 */
@Suppress("PROVIDED_RUNTIME_TOO_LOW") // https://github.com/Kotlin/kotlinx.serialization/issues/993
@Serializable
data class HubConfiguration(
    override val server: ServerConfiguration
) : Configuration