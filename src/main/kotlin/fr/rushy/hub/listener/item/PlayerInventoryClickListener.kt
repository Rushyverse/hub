package fr.rushy.hub.listener.item

import fr.rushy.hub.items.hotbar.HotbarItemsManager
import net.minestom.server.event.EventListener
import net.minestom.server.event.inventory.InventoryPreClickEvent

class PlayerInventoryClickListener : EventListener<InventoryPreClickEvent> {

    override fun eventType(): Class<InventoryPreClickEvent> {
        return InventoryPreClickEvent::class.java
    }

    override fun run(event: InventoryPreClickEvent): EventListener.Result {
        val player = event.player
        val item = event.clickedItem

        event.isCancelled = true

        val customItemMap = HotbarItemsManager.customItemMap
        customItemMap[item.material()]?.onClick(player)

        return EventListener.Result.SUCCESS
    }
}