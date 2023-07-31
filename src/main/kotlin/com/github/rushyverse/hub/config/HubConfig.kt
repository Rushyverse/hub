package com.github.rushyverse.hub.config

import com.github.rushyverse.hub.config.game.GamesGUIConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Material

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class HubConfig(
    val hotbar: HotbarConfig,
    val gamesGUI: GamesGUIConfig
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
@SerialName("hotbar")
data class HotbarConfig(
    val navigatorItem: HotbarItemConfig
) {
    val items: Set<HotbarItemConfig> = setOf(navigatorItem)
}

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class HotbarItemConfig(
    val type: Material,
    val name: String,
    val description: String,
    val hotbarSlot: Int,
)