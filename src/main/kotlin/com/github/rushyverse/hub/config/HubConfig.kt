package com.github.rushyverse.hub.config

import com.github.rushyverse.hub.config.game.GamesGUIConfig
import kotlinx.serialization.Serializable
import org.bukkit.Material

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class HubConfig(
    val hotbar: Set<HotbarItemConfig>,
    val visibilityCommand: VisibilityCommandConfig,
    val gamesGUI: GamesGUIConfig
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class HotbarItemConfig(
    val type: Material,
    val name: String,
    val description: String,
    val hotbarSlot: Int,
    val commandOnClick: String
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class VisibilityCommandConfig(
    val item: VisibilityItemConfig,
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class VisibilityItemConfig(
    val slot: Int,
    val materialOn: Material,
    val materialOff: Material,
    val enabled: Boolean
)