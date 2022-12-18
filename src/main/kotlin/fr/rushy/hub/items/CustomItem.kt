package fr.rushy.hub.items

import fr.rushy.invext.Clickable
import net.kyori.adventure.text.Component
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

abstract class CustomItem(val material: Material, val name: String, val lore:String?) : Clickable {

    constructor(
        material: Material,
        name: String,
    ) : this(material, name, null)

    fun toItem(): ItemStack {
        return ItemStack.of(material, 1).with {
            it.displayName(Component.text(name))

            if (lore != null) {
                it.lore(Component.text(lore))
            }
        }
    }
}