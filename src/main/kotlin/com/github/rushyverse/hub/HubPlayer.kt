package com.github.rushyverse.hub

import com.github.rushyverse.api.translation.SupportedLanguage
import net.minestom.server.entity.Player
import net.minestom.server.network.player.PlayerConnection
import java.util.*

fun Player.setLocale(language: SupportedLanguage) {
    this.locale = language.locale
}

class HubPlayer(
    uuid: UUID,
    username: String,
    playerConnection: PlayerConnection,
    var prestige: Int = 0,
    var tokens: Int = 0,
    var experience: Double = 0.0,
    var friends: Set<UUID> = emptySet(),
    var guildID: Long = 0
) : Player(uuid, username, playerConnection)