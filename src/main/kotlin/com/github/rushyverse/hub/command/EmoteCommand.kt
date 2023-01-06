package com.github.rushyverse.hub.command

import com.github.rushyverse.hub.emotes.DabEmote
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.PlayerSkin

class EmoteCommand : Command("emote") {

    init {
        setDefaultExecutor { sender, context ->
            val player = sender.asPlayer()
            val instance = player.instance
            val pos = player.position
            val skin = PlayerSkin.fromUsername(player.username);
            DabEmote(instance, pos, skin)
        }
    }

}