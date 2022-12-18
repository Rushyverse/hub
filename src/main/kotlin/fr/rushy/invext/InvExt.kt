package fr.rushy.invext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

// The system is built to handle click on items by slot.

fun Inventory.cancelInteractions() {
    this.addInventoryCondition { _, _, _, inventoryConditionResult: InventoryConditionResult ->
        inventoryConditionResult.isCancel = true
    }
}

fun Inventory.setClickableItem(slot: Int, item: ItemStack, callback: Clickable) {
    this.setItemStack(slot, item)
    this.registerClickEvent(slot, callback)
}

private fun isSlotEmpty(inventory: Inventory, slot: Int): Boolean {
    val item = inventory.getItemStack(slot);
    return item == null || item.isAir
}

fun Inventory.addClickableItem(item:ItemStack, callback: Clickable) : Int{

    var slot = 0;
    while (!isSlotEmpty(this, slot)) {
        println("item in slot $slot is not null")
        slot++
    }

    if (slot > 53) {
        throw Exception("Inventory is full")
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
    val backItem = ItemStack.of(Material.ARROW, 1)
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
    this.setClickableItem(slot, backItem, object : Clickable {
        override fun onClick(player: Player) {
            player.openInventory(backInventory)
        }
    })
}

fun Inventory.setCloseButton(slot: Int) {
    val closeItem = ItemStack.of(Material.BARRIER, 1)
        .withDisplayName(Component.text("❌").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
    this.setClickableItem(slot, closeItem, object : Clickable {
        override fun onClick(player: Player) {
            player.closeInventory()
        }
    })
}