package fr.rushy.invext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material


/**
 * Cancel interactions on this inventory.
 */
fun Inventory.cancelInteractions() {
    this.addInventoryCondition { _, _, _, inventoryConditionResult: InventoryConditionResult ->
        inventoryConditionResult.isCancel = true
    }
}

/**
 * Set a clickable ItemStack on the given slot.
 */
fun Inventory.setClickableItem(slot: Int, item: ItemStack, callback: Clickable) {
    this.setItemStack(slot, item)
    this.registerClickEvent(slot, callback)
}

fun Inventory.isSlotEmpty(slot: Int): Boolean {
    return getItemStack(slot).isAir
}

fun Inventory.firstAvailableSlot(): Int {
    return (0 until size).indexOfFirst { isSlotEmpty(it) }
}

fun Inventory.addClickableItem(item: ItemStack, callback: Clickable): Int? {
    val slot = firstAvailableSlot()
    if (slot == -1) {
        return null
    }

    this.setClickableItem(slot, item, callback)
    return slot
}


fun Inventory.registerClickEvent(slot: Int, callback: Clickable) {
    addInventoryCondition { player: Player,
                            clickedSlot: Int,
                            _,
                            _ ->
        if (clickedSlot == slot) {
            callback.onClick(player)
        }
    }
}

fun Inventory.setBackButton(slot: Int, backInventory: Inventory) {
    val backInvTitle = backInventory.title
    val backItem = ItemStack.of(Material.ARROW)
        .withDisplayName(
            Component.text("˿").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true)
                .append(
                    backInvTitle
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, true)
                        .decoration(TextDecoration.BOLD, false)
                )
        )
    setClickableItem(slot, backItem) { it.openInventory(backInventory) }
}

fun Inventory.setCloseButton(slot: Int) {
    val closeItem = ItemStack.of(Material.BARRIER)
        .withDisplayName(Component.text("❌").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))

    setClickableItem(slot, closeItem) { it.closeInventory() }
}