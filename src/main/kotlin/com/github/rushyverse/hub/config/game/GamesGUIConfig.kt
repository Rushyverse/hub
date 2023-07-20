package com.github.rushyverse.hub.config.game

import com.github.rushyverse.hub.config.ItemConfig
import com.github.rushyverse.api.extension.getIntOrException
import com.github.rushyverse.api.extension.getMaterialOrException
import com.github.rushyverse.api.extension.getSectionOrException
import com.github.rushyverse.api.extension.getStringOrException
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

data class GamesGUIConfig(
    val dataProvider: DataProviderConfig,
    val games: List<GameGUIConfig>
) {


    companion object {
        fun parse(section: ConfigurationSection): GamesGUIConfig {
            val dataProvider = section.getSectionOrException("data-provider")

            val gamesSection = section.getSectionOrException("games")
            val games = gamesSection.getKeys(false)
            val gamesConfigList = mutableListOf<GameGUIConfig>()

            games.forEach {
                gamesConfigList.add(
                    GameGUIConfig.parse(
                        it,
                        gamesSection.getSectionOrException(it)
                    )
                )
            }

            return GamesGUIConfig(
                DataProviderConfig.parse(dataProvider),
                gamesConfigList
            )
        }
    }
}


data class GameGUIConfig(
    val gameType: String,
    val icon: GameIconConfig,
    val onCLickGame: OnClickGameConfig
) {
    /**
     * TODO: plugin-message
     * TODO: redis-message
     */
    fun clickGameCommand(gameIndex: Int) =
        onCLickGame.runCommand.command.replace(
            "<gameID>", "$gameIndex"
        )

    companion object {
        fun parse(gameName: String, section: ConfigurationSection): GameGUIConfig {
            val iconSection = section.getSectionOrException("icon")
            val onClickGameSection = section.getSectionOrException("on-click-game")

            return GameGUIConfig(
                gameName,
                GameIconConfig.parse(iconSection),
                OnClickGameConfig.parse(onClickGameSection)
            )
        }
    }
}

data class GameIconConfig(
    override val type: Material,
    override val name: String,
    override val description: String,
    val menuSlot: Int
) : ItemConfig(type, name, description) {
    companion object {
        fun parse(section: ConfigurationSection) = GameIconConfig(
            section.getMaterialOrException("type"),
            section.getStringOrException("name"),
            section.getStringOrException("description"),
            section.getIntOrException("menu-slot")
        )
    }
}

data class OnClickGameConfig(
    val runCommand: RunCommand,
    val pluginMessage: PluginMessage,
    val redisMessage: RedisMessage
) {
    companion object {
        fun parse(section: ConfigurationSection): OnClickGameConfig {
            val runCommandSection = section.getSectionOrException("run-command")
            val pluginMessageSection = section.getSectionOrException("plugin-message")
            val redisMessageSection = section.getSectionOrException("redis-message")

            return OnClickGameConfig(
                RunCommand.parse(runCommandSection),
                PluginMessage.parse(pluginMessageSection),
                RedisMessage.parse(redisMessageSection)
            )
        }
    }

    data class RunCommand(
        val command: String,
        val enabled: Boolean
    ) {
        companion object {
            fun parse(section: ConfigurationSection) = RunCommand(
                section.getStringOrException("command"),
                section.getBoolean("enabled")
            )
        }
    }

    data class PluginMessage(
        val message: String,
        val enabled: Boolean
    ) {
        companion object {
            fun parse(section: ConfigurationSection) = PluginMessage(
                section.getStringOrException("message"),
                section.getBoolean("enabled")
            )
        }
    }

    data class RedisMessage(
        val message: String,
        val enabled: Boolean
    ) {
        companion object {
            fun parse(section: ConfigurationSection) = RedisMessage(
                section.getStringOrException("message"),
                section.getBoolean("enabled")
            )
        }
    }
}
