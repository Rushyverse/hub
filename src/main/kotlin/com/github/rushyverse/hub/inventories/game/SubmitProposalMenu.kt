package com.github.rushyverse.hub.inventories.game

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player

class SubmitProposalMenu(val player: Player, private val game: String) {

    fun get(): Book {
        return Book.builder()
            .pages(Component.text("Ceci est un test"))
            .author(player.name)
            .title(Component.text("Proposition pour le jeu $game"))
            .build()
    }
}
