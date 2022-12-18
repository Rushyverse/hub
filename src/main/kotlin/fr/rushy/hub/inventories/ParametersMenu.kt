package fr.rushy.hub.inventories

import fr.rushy.invext.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class ParametersMenu(val player: Player) : Inventory(InventoryType.CHEST_1_ROW, "Paramètres utilisateur") {

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

    init {


        // For each parameter, create an item and add it to the inventory
        for (parameter in ParameterType.values()) {
            val item = createParameterItem(parameter, ParameterValue.ALWAYS)


            val slot = addClickableItem(item, object : Clickable {
                override fun onClick(player: Player) {
                    val currentValue = ParameterValue.ALWAYS
                    val currentValIndex = parameter.acceptedValues.indexOf(currentValue)
                    val nextValIndex = currentValIndex + 1;

                    var nextValue: ParameterValue = if (nextValIndex == (parameter.acceptedValues.size - 1)) {
                        parameter.acceptedValues[0]
                    } else {
                        parameter.acceptedValues[nextValIndex]
                    }

                    updatePlayerParameter(player, parameter, nextValue);
                   //  setClickableItem(parameter.ordinal, createParameterItem(parameter, nextValue), this)
                }
            })
        }

        setCloseButton(8)
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

    private fun updateItemLore(item: ItemStack, type: ParameterType, newValue: ParameterValue) {
        val loreList = formatLore(type, newValue)
    }

    private fun updatePlayerParameter(
        player: Player,
        parameter: ParametersMenu.ParameterType,
        nextValue: ParametersMenu.ParameterValue
    ) {
        player.sendMessage(Component.text("(§ETODO§F) Parameter ${parameter.parameterName} updated to ${nextValue.name}"))
    }

    fun createParameterItem(parameter: ParameterType, value: ParameterValue): ItemStack {

        val material = parameter.material
        val loreList = formatLore(parameter, value)

        return ItemStack.of(material, value.ordinal + 1)
            .withDisplayName(Component.text(parameter.parameterName)).withLore(loreList)
    }
}