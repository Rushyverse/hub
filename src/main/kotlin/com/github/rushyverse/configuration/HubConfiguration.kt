package com.github.rushyverse.configuration

import com.github.rushyverse.api.configuration.*
import com.github.rushyverse.api.serializer.PosSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.coordinate.Pos

/**
 * Configuration of the server.
 * @property server Configuration about the minestom server.
 * @property area Configuration about the locations.
 */
@Suppress("PROVIDED_RUNTIME_TOO_LOW") // https://github.com/Kotlin/kotlinx.serialization/issues/993
@Serializable
data class HubConfiguration(
    @SerialName("server")
    override val server: ServerConfiguration,
    @SerialName("area")
    val area: AreaConfiguration,
) : IConfiguration

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class ServerConfiguration(
    override val port: Int,
    override val world: String,
    override val onlineMode: Boolean,
    override val bungeeCord: BungeeCordConfiguration,
    override val velocity: VelocityConfiguration
) : IServerConfiguration

/**
 * Area configuration for positions and values.
 * @property limitY Corresponds to the limit not to be exceeded when falling.
 * @property spawnPoint Represents the spawn point of the game server.
 */
@Serializable
data class AreaConfiguration(
    val limitY: Double,
    @Serializable(with = PosSerializer::class)
    val spawnPoint: Pos
)