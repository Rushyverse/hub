package com.github.rushyverse.hub.config

import com.github.rushyverse.api.extension.getMaterialOrException
import com.github.rushyverse.api.extension.getStringOrException
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

open class ItemConfig(
    open val type: Material,
    open val name: String,
    open val description: String
) {
    companion object {
        fun parse(section: ConfigurationSection): ItemConfig {
            val type = section.getMaterialOrException("type")
            val name = section.getStringOrException("name")
            val description = section.getStringOrException("description")
            return ItemConfig(type, name, description)
        }
    }
}