package com.github.rushyverse.hub.extension

import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.config.ItemConfig
import com.github.rushyverse.api.extension.ItemStack
import com.github.rushyverse.api.extension.toFormattedLore
import com.github.rushyverse.api.extension.toLore
import com.github.rushyverse.api.translation.SupportedLanguage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Utility method to build [ItemStack] from [ItemConfig].
 * @param config Configuration class
 * @param loreAdd Lore add after the config, optional
 * @return Instance of the item who is created.
 */
public inline fun ItemStack(
    config: ItemConfig,
    vararg loreAdd: Component,
    locale: Locale = SupportedLanguage.ENGLISH.locale
): ItemStack {
    return ItemStack(config.type) {
        itemMeta = itemMeta.apply {
            var configName = config.name

            if (configName.contains(".")) {
                configName = Hub.translationsProvider.translate(configName, locale, BUNDLE_HUB)
            }
            displayName(Component.text(configName, NamedTextColor.AQUA))

            var configDescription = config.description
            if (configDescription.contains(".")) {
                configDescription = Hub.translationsProvider.translate(configDescription, locale, BUNDLE_HUB)
            }

            lore(listOf(
                *configDescription.toFormattedLore().toLore().toTypedArray(),
                *loreAdd
            ))
        }
    }
}