package com.github.rushyverse.hub.data

import com.github.rushyverse.api.extension.BukkitRunnable
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.enums.CosmeticType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.concurrent.timerTask

sealed class CosmeticData(
    val name: String,
    val description: String,
    val type: CosmeticType,
    var material: Material
) {
    open fun runEffect(player: Player, plugin: Hub, delay: Long) {}

    object Particles {

        data object Light : CosmeticData("light", "Light", CosmeticType.PARTICLES, Material.GLOWSTONE_DUST)
        data object RedSwirls : CosmeticData("redswirls", "Red Swirls", CosmeticType.PARTICLES, Material.POPPY)
        data object GreenAura : CosmeticData("greenaura", "Green Aura", CosmeticType.PARTICLES, Material.GREEN_DYE)
        data object BlackForce : CosmeticData("blackforce", "Black Force", CosmeticType.PARTICLES, Material.OBSIDIAN)
        data object FireSparks :
            CosmeticData("firesparks", "Fire Sparks", CosmeticType.PARTICLES, Material.BLAZE_POWDER)

        data object BloodDrips : CosmeticData("blooddrips", "Blood Drips", CosmeticType.PARTICLES, Material.REDSTONE)
        data object CherryBlossoms :
            CosmeticData("cherryblossoms", "Cherry Blossoms", CosmeticType.PARTICLES, Material.PINK_DYE)

        data object MagmaFlow : CosmeticData("magmaflow", "Magma Flow", CosmeticType.PARTICLES, Material.MAGMA_CREAM)
        data object Hearts : CosmeticData("hearts", "Hearts", CosmeticType.PARTICLES, Material.PINK_TULIP)
        data object DragonBreath :
            CosmeticData("dragonbreath", "Dragon Breath", CosmeticType.PARTICLES, Material.DRAGON_BREATH)

        data object ExplosionDebris :
            CosmeticData("explosionDebris", "Explosion Debris", CosmeticType.PARTICLES, Material.TNT)

        data object MagicRunes :
            CosmeticData("magicRunes", "Magic Runes", CosmeticType.PARTICLES, Material.ENCHANTED_BOOK)

        data object PoisonCloud :
            CosmeticData("poisonCloud", "Poison Cloud", CosmeticType.PARTICLES, Material.WHITE_BANNER)

        data object RedNebula :
            CosmeticData("redNebula", "Red Nebula", CosmeticType.PARTICLES, Material.RED_STAINED_GLASS)

        data object FlameWhirlwind :
            CosmeticData("flameWhirlwind", "Flame Whirlwind", CosmeticType.PARTICLES, Material.BLAZE_POWDER)

        data object MysticalEmbers :
            CosmeticData("mysticalEmbers", "Mystical Embers", CosmeticType.PARTICLES, Material.ENDER_PEARL)

        data object VortexOfSouls :
            CosmeticData("vortexOfSouls", "Vortex Of Souls", CosmeticType.PARTICLES, Material.ENDER_EYE)

        data object RedLightning :
            CosmeticData("redLightning", "Red Lightning", CosmeticType.PARTICLES, Material.RED_DYE)

    }

    object Gadgets {

        data object GrapplingHook :
            CosmeticData("grapplingHook", "Grappling Hook", CosmeticType.GADGETS, Material.FISHING_ROD)

        data object SnowWand : CosmeticData("snowWand", "Snow Wand", CosmeticType.GADGETS, Material.BLAZE_ROD)
        data object FireworkBow : CosmeticData("fireworkBow", "Firework Bow", CosmeticType.GADGETS, Material.BOW)
        data object EnderButt : CosmeticData("enderButt", "Ender Butt", CosmeticType.GADGETS, Material.ENDER_PEARL)
        data object KnockbackStick :
            CosmeticData("knockbackStick", "Knockback Stick", CosmeticType.GADGETS, Material.STICK)

    }

    object Hats {

        data object AstroHat : CosmeticData("astroHat", "Astro Hat", CosmeticType.HATS, Material.GLASS)
        data class RainbowHat(var colorIndex: Int = 0) :
            CosmeticData("rainbowHat", "Rainbow Hat", CosmeticType.HATS, Material.RED_STAINED_GLASS) {
            private val colors = arrayOf(
                Material.RED_STAINED_GLASS,
                Material.ORANGE_STAINED_GLASS,
                Material.YELLOW_STAINED_GLASS,
                Material.LIME_STAINED_GLASS,
                Material.GREEN_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS,
                Material.PURPLE_STAINED_GLASS,
                Material.MAGENTA_STAINED_GLASS
            )

            override fun runEffect(player: Player, plugin: Hub, delay: Long) {
                BukkitRunnable {
                    if (player.isOnline) {
                        updateColor()
                        player.inventory.helmet?.type = material
                    } else {
                        cancel()
                    }
                }.runTaskTimer(plugin, delay, 0)
            }

            private fun updateColor() {
                colorIndex = (colorIndex + 1) % colors.size
                material = colors[colorIndex]
            }
        }

        data object LampHat : CosmeticData("lampHat", "Lamp Hat", CosmeticType.HATS, Material.GLOWSTONE)
        data object SuperHat : CosmeticData("superHat", "Super Hat", CosmeticType.HATS, Material.BLACK_WOOL)
        data object CrimsonHat : CosmeticData("crimsonHat", "Crimson Hat", CosmeticType.HATS, Material.REDSTONE_ORE)
    }
}