package fr.rushy.invext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.item.ItemStack


/**
 * Converts a string to a [Component] with the default color and decoration.
 * Use the '\n' character to create a new line.
 *
 * @param string The string to convert.
 * @return The [Component] converted.
 */
fun formatLoreUsingSplit(lore: String): List<Component> {
    val list = mutableListOf<Component>()

    lore.split("\n").forEach {
        list.add(Component.text(it).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
    }

    return list;
}

/**
 * Apply a lore from string with default lore style using the [formatLoreUsingSplit] function.
 * Use the '\n' character to create a new line.
 * @param lore The lore to apply.
 */
fun ItemStack.withLore(lore: String): ItemStack = withLore(formatLoreUsingSplit(lore))
