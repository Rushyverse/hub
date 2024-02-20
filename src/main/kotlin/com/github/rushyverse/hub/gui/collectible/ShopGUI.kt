package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.gui.ItemStackIndex
import com.github.rushyverse.api.gui.PlayerGUI
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.extension.ItemStack
import com.github.rushyverse.hub.gui.commons.GUI
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import java.util.*

class ShopGUI(plugin: Plugin, private val translator: Translator) : PlayerGUI() {

    private val hatGUI = HatGUI(plugin, translator)
    private val particleGUI = ParticleGUI(plugin, translator)
    private val gadgetGUI = GadgetGUI(plugin, translator)

    override suspend fun register(): Boolean {
        return super.register().also {
            hatGUI.register()
            particleGUI.register()
            gadgetGUI.register()
        }
    }

    override suspend fun close() {
        hatGUI.close()
        particleGUI.close()
        gadgetGUI.close()
        super.close()
    }

    override suspend fun createInventory(owner: InventoryHolder, client: Client): Inventory {
        return server.createInventory(owner, 27, translator.getComponent("gui.shop.title", client.lang().locale))
    }

    override suspend fun applyItems(client: Client, inv: Inventory) {
        val locale = client.lang().locale
        inv.setItem(10, createHatsGroup(locale))
        inv.setItem(12, createParticlesGroup(locale))
        inv.setItem(14, createGadgetsGroup(locale))
        inv.setItem(16, createSpecialGroup(locale))
    }

    override suspend fun onClick(client: Client, item: ItemStack, event: InventoryClickEvent) {
        client.lang().locale

        when (item.type) {
            Material.IRON_HELMET ->
                hats.open(client)

            Material.NETHER_STAR -> {
                particles.open(client)
            }

            Material.COMPARATOR -> {
                gadgets.open(client)
            }

            Material.LEATHER -> {
                client.player?.closeInventory()
                client.send("New cosmetics incoming...")
            }

            else -> {
                return
            }
        }
    }

    private fun createHatsGroup(locale: Locale) = ItemStack(
        type = Material.IRON_HELMET,
        name = "shop.hatsgroup.name",
        description = "shop.hatsgroup.desc",
        locale = locale,
        translator = translator,
    )

    private fun createParticlesGroup(locale: Locale) = ItemStack(
        type = Material.NETHER_STAR,
        name = "shop.particlesgroup.name",
        description = "shop.particlesgroup.desc",
        locale = locale,
        translator = translator,
    )


    private fun createGadgetsGroup(locale: Locale) = ItemStack(
        type = Material.COMPARATOR,
        name = "shop.gadgetsgroup.name",
        description = "shop.gadgetsgroup.desc",
        locale = locale,
        translator = translator,
    )

    private fun createSpecialGroup(locale: Locale) = ItemStack(
        type = Material.LEATHER,
        name = "shop.specialgroup.name",
        description = "shop.specialgroup.desc",
        locale = locale,
        translator = translator,
    )
}