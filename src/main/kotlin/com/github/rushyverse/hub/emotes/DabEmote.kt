package com.github.rushyverse.hub.emotes

import com.google.gson.JsonObject
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.instance.Instance
import net.worldseed.gestures.EmotePlayer
import net.worldseed.multipart.ModelLoader


class DabEmote : EmotePlayer {
    private val ANIMATION_STRING =
        "{\"format_version\":\"1.8.0\",\"animations\":{\"dab\":{\"animation_length\":2,\"bones\":{\"_head\":{\"rotation\":{\"0.0\":[0,0,0],\"0.45\":[32.5,0,0],\"1.45\":[32.5,0,0],\"1.8\":[0,0,0]}},\"right_arm\":{\"rotation\":{\"0.0\":[0,0,0],\"0.45\":[-47.5,0,-100],\"0.85\":[0,0,100],\"1.25\":[-47.5,0,-100],\"1.8\":[0,0,0]}},\"left_arm\":{\"rotation\":{\"0.0\":[0,0,0],\"0.45\":[0,0,-100],\"0.85\":[-47.5,0,100],\"1.25\":[0,0,-100],\"1.8\":[0,0,0]}},\"left_leg\":{\"rotation\":{\"0.0\":[0,0,0],\"1.45\":[10,0,0],\"1.8\":[0,0,0]}},\"torso\":{\"rotation\":{\"0.0\":[0,0,0],\"1.45\":[5,0,0],\"1.8\":[0,0,0]}}}},\"wave\":{\"animation_length\":4,\"bones\":{\"right_arm\":{\"rotation\":{\"0.0\":[0,0,0],\"0.3\":[0,0,137.5],\"4.0\":[0,0,0]},\"position\":{\"0.0\":[0,0,0],\"0.3\":[-0.25,0,0],\"4.0\":[0,0,0]}}}}},\"geckolib_format_version\":2}"
    private var ANIMATIONS: Map<String, JsonObject>? = ModelLoader.parseAnimations(ANIMATION_STRING)

    constructor(instance: Instance?, pos: Pos?, skin: PlayerSkin?) : super(instance, pos, skin) {
        loadEmotes(ANIMATIONS)
        animationHandler.playRepeat("dab")
    }
}