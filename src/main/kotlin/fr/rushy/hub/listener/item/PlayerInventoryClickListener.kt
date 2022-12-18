package fr.rushy.hub.listener.item

import fr.rushy.hub.items.hotbar.HotbarItemsManager
import fr.rushy.hub.items.hotbar.HotbarItemsManager.customItemMap
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryPreClickEvent

class PlayerInventoryClickListener : EventListener<InventoryPreClickEvent> {

    override fun eventType(): Class<InventoryPreClickEvent> {
        return InventoryPreClickEvent::class.java
    }

    override fun run(event: InventoryPreClickEvent): EventListener.Result {
        val player = event.player
        val item = event.clickedItem

        // Always true for security (inventory glitch etc..)
        event.isCancelled = true

        val isPlayerInventory = event.inventory == null

        if (isPlayerInventory) { // Handle only items are in the player inventory, not others.
            val customItemMap = HotbarItemsManager.customItemMap
            customItemMap[item.material()]?.onClick(player)
        }

        return EventListener.Result.SUCCESS
    }
}