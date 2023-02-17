package com.github.rushyverse.hub.inventories

import net.minestom.server.inventory.Inventory

fun interface IMenu {
    suspend fun build(): Inventory
}