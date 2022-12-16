package fr.rushy.hub.listener

import fr.rushy.hub.items.hotbar.HotbarItemsManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.scoreboard.Sidebar

class PlayerSpawnListener : EventListener<PlayerSpawnEvent> {

    override fun eventType(): Class<PlayerSpawnEvent> {
        return PlayerSpawnEvent::class.java
    }

    override fun run(event: PlayerSpawnEvent): EventListener.Result {
        val player = event.player

        // Scoreboard
        val sidebar = Sidebar(Component.text("Sidebar"))
        sidebar.createLine(
            Sidebar.ScoreboardLine(
                "score",
                Component.text("test", NamedTextColor.RED),
                0
            ))
        sidebar.addViewer(player)

        // Teleport and give items
        player.teleport(Pos(0.0,2.0,0.0))

        player.inventory.setItemStack(3, HotbarItemsManager.getStatsMenuItem().toItem())
        player.inventory.setItemStack(4, HotbarItemsManager.getMainMenuItem().toItem())
        player.inventory.setItemStack(5, HotbarItemsManager.getCosmeticsMenuItem().toItem())

        return EventListener.Result.SUCCESS
    }
}