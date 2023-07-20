package com.github.rushyverse.hub.config

import com.github.rushyverse.hub.config.game.GamesGUIConfig
import com.github.rushyverse.api.extension.getSectionOrException
import org.bukkit.configuration.file.FileConfiguration

data class HubConfig(
    val hotbar: HotbarConfig,
    val gamesMenu: GamesGUIConfig
) {

    companion object {
        fun parse(config: FileConfiguration): HubConfig {
            val hotbarConfig = config.getSectionOrException("hotbar")
            val gamesMenuConfig = config.getSectionOrException("games-menu")


            return HubConfig(
                HotbarConfig.parse(hotbarConfig),
                GamesGUIConfig.parse(gamesMenuConfig)
            )
        }
    }
}
