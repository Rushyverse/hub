package fr.rushy.hub.configuration

import fr.rushy.api.configuration.IConfiguration
import fr.rushy.api.configuration.IServerConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration of the server.
 * @property server Configuration about the minestom server.
 */
@Suppress("PROVIDED_RUNTIME_TOO_LOW") // https://github.com/Kotlin/kotlinx.serialization/issues/993
@Serializable
data class HubConfiguration(
    @SerialName("server")
    override val server: ServerConfiguration
) : IConfiguration

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class ServerConfiguration(
    override val port: Int,
    override val world: String
) : IServerConfiguration