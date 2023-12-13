package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.hub.data.Cosmetic
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class ParticleGUI : CosmeticGUI("gui.particles.title", 54) {

    override val cosmetics = listOf(
        10 to Cosmetic.Particles.Light,
        11 to Cosmetic.Particles.RedSwirls,
        12 to Cosmetic.Particles.GreenAura,
        13 to Cosmetic.Particles.BlackForce,
        14 to Cosmetic.Particles.FireSparks,
        15 to Cosmetic.Particles.BloodDrips,
        16 to Cosmetic.Particles.CherryBlossoms,

        19 to Cosmetic.Particles.MagmaFlow,
        20 to Cosmetic.Particles.Hearts,
        21 to Cosmetic.Particles.DragonBreath,
        22 to Cosmetic.Particles.ExplosionDebris,
        23 to Cosmetic.Particles.MagicRunes,
        24 to Cosmetic.Particles.PoisonCloud,
        25 to Cosmetic.Particles.RedNebula,

        28 to Cosmetic.Particles.FlameWhirlwind,
        29 to Cosmetic.Particles.MysticalEmbers,
        30 to Cosmetic.Particles.VortexOfSouls,
        31 to Cosmetic.Particles.RedLightning,
    )

    override fun setItem(cosmeticItem: ItemStack, inventory: PlayerInventory, event: InventoryClickEvent) {
        inventory.setItem(CosmeticGUI.COSMETIC_PLAYER_INV_SLOT, cosmeticItem)
    }
}