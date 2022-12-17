package fr.rushy.hub.items

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

abstract class CustomItem(val material: Material, val name: String) : Clickable {

    fun toItem(): ItemStack {
        return ItemStack.of(material, 1).with {
            it.displayName(Component.text(name))
        }
    }
}