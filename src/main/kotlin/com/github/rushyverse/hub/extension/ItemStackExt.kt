package com.github.rushyverse.hub.extension

import com.github.rushyverse.api.extension.ItemStack
import com.github.rushyverse.api.extension.toFormattedLore
import com.github.rushyverse.api.extension.toLore
import com.github.rushyverse.api.extension.withoutDecorations
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.Translator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Utility method to build [ItemStack] with translation system.
 * @param type The type of Material.
 * @param name The name of the item. Can be a translation key in properties files.
 * @param description The description of the item. Can also be a translation key.
 * @param loreAdd Lore add after the config, optional
 * @return Instance of the item who is created.
 */
fun ItemStack(
    type: Material, name: String, description: String,
    vararg loreAdd: Component,
    locale: Locale = SupportedLanguage.ENGLISH.locale,
    translator: Translator
): ItemStack {
    var itemName = name
    return ItemStack(type) {
        itemMeta = itemMeta.apply {

            if (itemName.contains(".")) {
                itemName = translator.get(itemName, locale)
            }
            displayName(Component.text(itemName, NamedTextColor.LIGHT_PURPLE).withoutDecorations())

            var configDescription = description
            if (configDescription.contains(".")) {
                configDescription = translator.get(configDescription, locale)
            }

            lore(
                listOf(
                    *configDescription.toFormattedLore().toLore().toTypedArray(),
                    *loreAdd
                )
            )
        }
    }
}