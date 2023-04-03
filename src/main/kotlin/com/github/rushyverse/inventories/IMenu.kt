package com.github.rushyverse.inventories

import net.minestom.server.inventory.Inventory

/**
 * Represents a custom menu that can be interpreted by the system.
 */
fun interface IMenu {
    suspend fun build(): Inventory
}