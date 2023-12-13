package com.github.rushyverse.hub.data

import com.github.rushyverse.hub.enums.CosmeticType
import org.bukkit.Material

sealed class Cosmetic(
    val name: String,
    val description: String,
    val type: CosmeticType,
    val material: Material
) {

    object Particles {

        data object Light: Cosmetic("light", "Light", CosmeticType.PARTICLES, Material.GLOWSTONE_DUST)
        data object RedSwirls: Cosmetic("redswirls", "Red Swirls", CosmeticType.PARTICLES, Material.POPPY)
        data object GreenAura: Cosmetic("greenaura", "Green Aura", CosmeticType.PARTICLES, Material.GREEN_DYE)
        data object BlackForce: Cosmetic("blackforce", "Black Force", CosmeticType.PARTICLES, Material.OBSIDIAN)
        data object FireSparks: Cosmetic("firesparks", "Fire Sparks", CosmeticType.PARTICLES, Material.BLAZE_POWDER)
        data object BloodDrips: Cosmetic("blooddrips", "Blood Drips", CosmeticType.PARTICLES, Material.REDSTONE)
        data object CherryBlossoms: Cosmetic("cherryblossoms", "Cherry Blossoms", CosmeticType.PARTICLES, Material.PINK_DYE)
        data object MagmaFlow: Cosmetic("magmaflow", "Magma Flow", CosmeticType.PARTICLES, Material.MAGMA_CREAM)
        data object Hearts: Cosmetic("hearts", "Hearts", CosmeticType.PARTICLES, Material.PINK_TULIP)
        data object DragonBreath: Cosmetic("dragonbreath", "Dragon Breath", CosmeticType.PARTICLES, Material.DRAGON_BREATH)
        data object ExplosionDebris: Cosmetic("explosionDebris", "Explosion Debris", CosmeticType.PARTICLES, Material.TNT)
        data object MagicRunes: Cosmetic("magicRunes", "Magic Runes", CosmeticType.PARTICLES, Material.ENCHANTED_BOOK)
        data object PoisonCloud: Cosmetic("poisonCloud", "Poison Cloud", CosmeticType.PARTICLES, Material.WHITE_BANNER)
        data object RedNebula: Cosmetic("redNebula", "Red Nebula", CosmeticType.PARTICLES, Material.RED_STAINED_GLASS)
        data object FlameWhirlwind: Cosmetic("flameWhirlwind", "Flame Whirlwind", CosmeticType.PARTICLES, Material.BLAZE_POWDER)
        data object MysticalEmbers: Cosmetic("mysticalEmbers", "Mystical Embers", CosmeticType.PARTICLES, Material.ENDER_PEARL)
        data object VortexOfSouls: Cosmetic("vortexOfSouls", "Vortex Of Souls", CosmeticType.PARTICLES, Material.ENDER_EYE)
        data object RedLightning: Cosmetic("redLightning", "Red Lightning", CosmeticType.PARTICLES, Material.RED_DYE)

    }

    object Gadgets {

        data object GrapplingHook: Cosmetic("grapplingHook", "Grappling Hook", CosmeticType.GADGETS, Material.FISHING_ROD)
        data object SnowWand: Cosmetic("snowWand", "Snow Wand", CosmeticType.GADGETS, Material.BLAZE_ROD)
        data object FireworkBow: Cosmetic("fireworkBow", "Firework Bow", CosmeticType.GADGETS, Material.BOW)
        data object EnderButt: Cosmetic("enderButt", "Ender Butt", CosmeticType.GADGETS, Material.ENDER_PEARL)
        data object KnockbackStick: Cosmetic("knockbackStick", "Knockback Stick", CosmeticType.GADGETS, Material.STICK)

    }

    object Hats {

        data object AstroHat: Cosmetic("astroHat", "Astro Hat", CosmeticType.HATS, Material.GLASS)
        data object RainbowHat: Cosmetic("rainbowHat", "Rainbow Hat", CosmeticType.HATS, Material.RED_STAINED_GLASS)
        data object LampHat: Cosmetic("lampHat", "Lamp Hat", CosmeticType.HATS, Material.GLOWSTONE)
        data object SuperHat: Cosmetic("superHat", "Super Hat", CosmeticType.HATS, Material.BLACK_WOOL)
        data object CrimsonHat: Cosmetic("crimsonHat", "Crimson Hat", CosmeticType.HATS, Material.REDSTONE_ORE)
    }
}