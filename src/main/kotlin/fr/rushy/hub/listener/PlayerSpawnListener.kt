package fr.rushy.hub.listener

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.scoreboard.Sidebar

class PlayerSpawnListener : EventListener<PlayerSpawnEvent> {

    override fun eventType(): Class<PlayerSpawnEvent> {
        return PlayerSpawnEvent::class.java
    }

    override fun run(event: PlayerSpawnEvent): EventListener.Result {
        val player = event.player
        val sidebar = Sidebar(Component.text("Sidebar"))
        sidebar.createLine(
            Sidebar.ScoreboardLine(
                "score",
                Component.text("test", NamedTextColor.RED),
                0
            ))
        sidebar.addViewer(player)
        return EventListener.Result.SUCCESS
    }
}