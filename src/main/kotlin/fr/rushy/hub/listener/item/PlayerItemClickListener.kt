package fr.rushy.hub.listener.item

import fr.rushy.hub.items.hotbar.HotbarItemsManager
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerUseItemEvent

class PlayerItemClickListener : EventListener<PlayerUseItemEvent> {

    override fun eventType(): Class<PlayerUseItemEvent> {
        return PlayerUseItemEvent::class.java
    }

    override fun run(event: PlayerUseItemEvent): EventListener.Result {
        val player = event.player
        val item = event.itemStack

        val customItemMap = HotbarItemsManager.customItemMap
        val clickable = customItemMap[item.material()]

        if (clickable != null) {
            event.isCancelled = true
            clickable.onClick(player)
        }

        return EventListener.Result.SUCCESS
    }
}