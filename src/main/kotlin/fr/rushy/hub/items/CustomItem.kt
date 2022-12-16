package fr.rushy.hub.items

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

abstract class CustomItem {

    private val material: Material;
    private val name: String;

    constructor(material: Material, name: String) {
        this.material = material;
        this.name = name;
    }

    fun getMaterial(): Material {
        return material;
    }

    fun getName(): String {
        return name;
    }

    fun toItem(): ItemStack {
        return ItemStack.of(material, 1).with {
            it.displayName(Component.text(name))
        }
    }

    abstract fun onClick(player:Player);
}