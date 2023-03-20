package com.github.rushyverse

import com.github.rushyverse.api.translation.SupportedLanguage
import net.minestom.server.entity.Player
import net.minestom.server.network.player.PlayerConnection
import java.util.*

class HubPlayer(
    uuid: UUID,
    username: String,
    playerConnection: PlayerConnection,
    var language: SupportedLanguage = SupportedLanguage.ENGLISH,
    var prestige: Int = 0,
    var tokens: Int = 0,
    var experience: Double = 0.0,
    var friends: Set<UUID> = emptySet()
) : Player(uuid, username, playerConnection) {

    init {
        locale = language.locale
    }

    override fun getLocale(): Locale {
        return language.locale
    }
}