package com.github.rushyverse.hub.config

import com.github.rushyverse.api.extension.getIntOrException
import com.github.rushyverse.api.extension.getMaterialOrException
import com.github.rushyverse.api.extension.getSectionOrException
import com.github.rushyverse.api.extension.getStringOrException
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

data class HotbarConfig(
    val navigatorItem: HotbarItemConfig
) {
    val items = setOf(navigatorItem)

    companion object {
        fun parse(section: ConfigurationSection): HotbarConfig {
            val navigatorItem = section.getSectionOrException("navigator-item")

            return HotbarConfig(
                HotbarItemConfig.parse(navigatorItem)
            )
        }
    }
}

data class HotbarItemConfig(
    override val type: Material,
    override val name: String,
    override val description: String,
    val hotbarSlot: Int,
) : ItemConfig(type, name, description) {

    companion object {
        fun parse(section: ConfigurationSection): HotbarItemConfig {
            val type = section.getMaterialOrException("type")
            val name = section.getStringOrException("name")
            val description = section.getStringOrException("description")
            val hotbarSlot = section.getIntOrException("hotbar-slot")

            return HotbarItemConfig(type, name, description, hotbarSlot)
        }
    }
}

