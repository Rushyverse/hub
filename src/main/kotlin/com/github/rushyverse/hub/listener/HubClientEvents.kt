package com.github.rushyverse.hub.listener

import com.github.rushyverse.hub.Hub
import com.github.rushyverse.api.player.*
import net.kyori.adventure.text.Component
import java.util.concurrent.atomic.AtomicReference

class HubClientEvents(
    private val hub: Hub
) : PluginClientEvents() {

    override suspend fun onJoin(client: Client, joinMessage: AtomicReference<Component?>) {

        client.send("ClientHub created")

        hub.teleportHub(client)

        joinMessage.set(null)
    }


    override suspend fun onQuit(client: Client, quitMessage: AtomicReference<Component?>) {
    }
}