package fr.rushy.hub.items

import net.minestom.server.entity.Player

interface Clickable {

    fun onClick(player: Player);
}