package com.github.rushyverse.hub.inventories.player

import com.github.rushyverse.api.extension.addItemStack
import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class ParametersMenu(
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player
) : IMenu {

    /**
     * Enum to store all the parameters and their corresponding material and allowed values
     */
    enum class ParameterType(
        val parameterName: String,
        val material: Material,
        val acceptedValues: List<ParameterValue>
    ) {
        CHAT_VISIBILITY(
            "Visibilité du chat", Material.PAPER,
            listOf(
                ParameterValue.ALWAYS,
                ParameterValue.ONLY_FRIENDS
            )
        ),
        PLAYERS_VISIBILITY(
            "Visibilité des joueurs", Material.CLOCK,
            listOf(
                ParameterValue.ALWAYS,
                ParameterValue.ONLY_FRIENDS,
                ParameterValue.ONLY_GUILD_TEAMMATES
            )
        ),
        PRIVATE_MESSAGES(
            "Messages privés", Material.WRITABLE_BOOK,
            listOf(
                ParameterValue.ALWAYS,
                ParameterValue.ONLY_FRIENDS,
            )
        ),
        FRIEND_REQUESTS(
            "Demandes d'ami", Material.TOTEM_OF_UNDYING,
            listOf(
                ParameterValue.ALWAYS,
                ParameterValue.ONLY_GUILD_TEAMMATES
            )
        ),
        GUILD_INVITATIONS(
            "Invitations de guildes", Material.WHITE_BANNER,
            listOf(
                ParameterValue.ALWAYS,
                ParameterValue.ONLY_FRIENDS,
            )
        ),
    }

    /**
     * Enum to store all the parameter values
     */
    enum class ParameterValue(val valueName: String, val color: NamedTextColor) {
        ALWAYS("Toujours", NamedTextColor.WHITE),
        ONLY_FRIENDS("Amis uniquement", NamedTextColor.GREEN),
        ONLY_GUILD_TEAMMATES("Membres de la guilde uniquement", NamedTextColor.LIGHT_PURPLE);
    }

    override fun build(): Inventory {
        val title = translationsProvider.translate("parameters_menu_title", locale, HubServer.BUNDLE_HUB)
        val inv = Inventory(InventoryType.CHEST_1_ROW, title)

        // For each parameter, create an item and add it to the inventory
        for (parameter in ParameterType.values()) {
            val item = createParameterItem(parameter, ParameterValue.ALWAYS)

            inv.addItemStack(item) { player, _, _, _ ->
                val currentValue = ParameterValue.ALWAYS
                val currentValIndex = parameter.acceptedValues.indexOf(currentValue)
                val nextValIndex = currentValIndex + 1;

                val nextValue: ParameterValue = if (nextValIndex == (parameter.acceptedValues.size - 1)) {
                    parameter.acceptedValues[0]
                } else {
                    parameter.acceptedValues[nextValIndex]
                }

                updatePlayerParameter(player, parameter, nextValue);
            }
        }

        inv.setCloseButton(8)
        return inv
    }

    private fun formatLore(type: ParameterType, currentValue: ParameterValue): List<Component> {
        val loreList = type.acceptedValues.map {
            if (it == currentValue) {
                Component.text("✓ ${it.valueName}").decoration(TextDecoration.ITALIC, true)
            } else {
                Component.text(it.valueName).decoration(TextDecoration.ITALIC, false)
            }
                .color(it.color)

        }
        return loreList;
    }

    private fun updatePlayerParameter(
        player: Player,
        parameter: ParameterType,
        nextValue: ParameterValue
    ) {
        player.sendMessage(Component.text("(§ETODO§F) Parameter ${parameter.parameterName} updated to ${nextValue.name}"))
    }

    private fun createParameterItem(parameter: ParameterType, value: ParameterValue): ItemStack {

        val material = parameter.material
        val loreList = formatLore(parameter, value)

        return ItemStack.builder(material)
            .amount(value.ordinal + 1)
            .displayName(Component.text(parameter.parameterName))
            .lore(loreList)
            .build()
    }
}