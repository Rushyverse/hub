package com.github.rushyverse.hub.extension

import org.bukkit.World

fun World.isHub() : Boolean {
    return name.lowercase().contains("hub")
}