package com.github.rushyverse.hub.config.game

import com.github.rushyverse.api.extension.getSectionOrException
import com.github.rushyverse.api.extension.getStringOrException
import org.bukkit.configuration.ConfigurationSection

data class DataProviderConfig(
    val apiMode: Boolean,
    val pluginMessage: PluginMessageProvider,
    val redis: RedisProvider
) {
    companion object {
        fun parse(section: ConfigurationSection): DataProviderConfig {
            val apiMode = section.getBoolean("api-mode")
            val pluginMessageProvider = section.getSectionOrException("plugin-message")
            val redisProvider = section.getSectionOrException("redis-message")

            return DataProviderConfig(
                apiMode,
                PluginMessageProvider.parse(pluginMessageProvider),
                RedisProvider.parse(redisProvider)
            )
        }
    }

    data class PluginMessageProvider(
        val message: String,
        val enabled: Boolean
    ) {
        companion object {
            fun parse(section: ConfigurationSection): PluginMessageProvider {
                val message = section.getStringOrException("message")
                val enabled = section.getBoolean("enabled")
                return PluginMessageProvider(message, enabled)
            }
        }
    }

    data class RedisProvider(
        val enabled: Boolean
    ) {
        // TODO

        companion object {
            fun parse(section: ConfigurationSection): RedisProvider {
                val enabled = section.getBoolean("enabled")
                return RedisProvider(enabled)
            }
        }
    }
}