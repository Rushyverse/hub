package fr.rushy.invext

import net.minestom.server.entity.Player

interface Clickable {

    fun onClick(player: Player);
}