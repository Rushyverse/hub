package com.github.rushyverse.hub.client

import com.github.rushyverse.api.player.Client
import kotlinx.coroutines.CoroutineScope
import java.util.*

class ClientHub(
    uuid: UUID, scope:
    CoroutineScope
) : Client(uuid, scope)