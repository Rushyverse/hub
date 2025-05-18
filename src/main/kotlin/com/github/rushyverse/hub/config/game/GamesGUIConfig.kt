package com.github.rushyverse.hub.config.game

import kotlinx.serialization.Serializable
import org.bukkit.Material

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class GamesGUIConfig(
    val dataProvider: DataProviderConfig,
    val games: List<GameGUIConfig>
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class DataProviderConfig(
    val apiSharedMemory: Boolean
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class GameGUIConfig(
    val gameType: String,
    val icon: GameIconConfig,
    val joinCommand: String,
    val createCommand: String,
    val deleteCommand: String,
    val games: Int = 0,
) {

    fun joinGameCommand(gameIndex: Int) =
        joinCommand.replace("<gameID>", "$gameIndex")

    fun createGameCommand(gameIndex: Int) =
        createCommand
            .replace("<gameID>", "$gameIndex")

    fun deleteGameCommand(gameIndex: Int) =
        deleteCommand.replace("<gameID>", "$gameIndex")
}

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class GameIconConfig(
    val type: Material,
    val name: String,
    val description: String,
    val menuSlot: Int
)