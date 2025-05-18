package com.github.rushyverse.hub.gui.collectible

import RedSwirlsParticleTask
import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.data.CosmeticData
import com.github.rushyverse.hub.gui.collectible.runnables.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class ParticleGUI(plugin: Plugin, translator: Translator) : CosmeticGUI(plugin, translator, "gui.particles.title") {


    override val cosmetics = listOf(
        CosmeticDataInventory(10, CosmeticData.Particles.Light) {
            it.player?.run {
                it.particleTask = LightParticleTask(it).apply {
                    runTaskTimer(plugin, 0L, 2L)
                }
            }
        },
        CosmeticDataInventory(11, CosmeticData.Particles.RedSwirls) {
            it.player?.run {
                it.particleTask = RedSwirlsParticleTask(it).apply {
                        runTaskTimer(plugin, 0L, 2L)
                    }
            }
        },
        CosmeticDataInventory(12, CosmeticData.Particles.GreenAura) {
            it.player?.run {
                it.particleTask = GreenAuraParticleTask(it).apply {
                        runTaskTimer(plugin, 0L, 1L)
                    }
            }
        },
        CosmeticDataInventory(13, CosmeticData.Particles.BlackForce) {
            it.player?.run {
                it.particleTask = BlackForceParticleTask(it).apply {
                    runTaskTimer(plugin, 0L, 1L)
                }
            }
        },
        CosmeticDataInventory(14, CosmeticData.Particles.FireSparks) {
            it.player?.run {
                it.particleTask = FireSparksParticleTask(it).apply {
                    runTaskTimer(plugin, 0L, 1L)
                }
            }
        },
        CosmeticDataInventory(15, CosmeticData.Particles.BloodDrips) {
            it.player?.run {
                it.particleTask = BloodDripsParticleTask(it).apply {
                    runTaskTimer(plugin, 0L, 1L)
                }
            }
        },
        CosmeticDataInventory(16, CosmeticData.Particles.CherryBlossoms) {
            it.player?.run {
                it.particleTask = CherryBlossomParticleTask(it).apply {
                    runTaskTimer(plugin, 0L, 1L)
                }
            }
        },
        CosmeticDataInventory(19, CosmeticData.Particles.MagmaFlow) {
            it.player?.run {
                it.particleTask = MagmaFlowParticleTask(it).apply { runTaskTimer(plugin, 0L, 1L) }
            }
        },
        CosmeticDataInventory(20, CosmeticData.Particles.Hearts) { },
        CosmeticDataInventory(21, CosmeticData.Particles.DragonBreath) { },
        CosmeticDataInventory(22, CosmeticData.Particles.ExplosionDebris) { },
        CosmeticDataInventory(23, CosmeticData.Particles.MagicRunes) { },
        CosmeticDataInventory(24, CosmeticData.Particles.PoisonCloud) { },
        CosmeticDataInventory(25, CosmeticData.Particles.RedNebula) { },

        CosmeticDataInventory(28, CosmeticData.Particles.FlameWhirlwind) { },
        CosmeticDataInventory(29, CosmeticData.Particles.MysticalEmbers) { },
        CosmeticDataInventory(30, CosmeticData.Particles.VortexOfSouls) { },
        CosmeticDataInventory(31, CosmeticData.Particles.RedLightning) { },
    )

    override fun setItem(
        cosmeticItem: ItemStack,
        cosmetic: CosmeticDataInventory?,
        player: PlayerInventory,
        event: InventoryClickEvent
    ) {

    }

    override suspend fun close() {
        super.close()
    }

    override fun onUnselect(inventory: PlayerInventory, client: ClientHub) {
        inventory.setItem(
            COSMETIC_PLAYER_INV_SLOT,
            null
        )

        client.particleTask?.cancel()?.also { client.particleTask = null }
    }

    override fun isAlreadySelected(inventory: PlayerInventory, cosmeticItem: ItemStack): Boolean {
        return inventory.getItem(COSMETIC_PLAYER_INV_SLOT) == cosmeticItem
    }
}