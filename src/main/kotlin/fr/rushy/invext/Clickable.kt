package fr.rushy.invext

import net.minestom.server.entity.Player

/**
 * Clickable is an interface to handle click events.
 */
fun interface Clickable {

    fun onClick(player: Player);
}