package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.hub.extension.SimpleCooldown
import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.data.CosmeticData
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.ThreadLocalRandom

class GadgetGUI(plugin: Plugin, translator: Translator) : CosmeticGUI(plugin, translator, "gui.gadgets.title") {

    override val cosmetics: List<CosmeticDataInventory> = listOf(
        GrapplingHook(),
        SnowWand(),
        FireworkBow(plugin),
        EnderButt(plugin),
        KnockbackStick()
    )

    override fun setItem(
        cosmeticItem: ItemStack,
        cosmetic: CosmeticDataInventory?,
        inventory: PlayerInventory,
        event: InventoryClickEvent
    ) {
        inventory.setItem(COSMETIC_PLAYER_INV_SLOT, cosmeticItem)
    }

    override fun onUnselect(inventory: PlayerInventory, client: ClientHub) {
        inventory.setItem(COSMETIC_PLAYER_INV_SLOT, null)
    }

    override fun isAlreadySelected(inventory: PlayerInventory, cosmeticItem: ItemStack): Boolean {
        return inventory.getItem(COSMETIC_PLAYER_INV_SLOT) == cosmeticItem
    }
}

class GrapplingHook : CosmeticDataInventory(10, CosmeticData.Gadgets.GrapplingHook), Listener {

    @EventHandler
    fun onFish(event: PlayerFishEvent) {
        val player = event.player
        // Verify if the item used is on the cosmetic slot
        if (player.inventory.heldItemSlot != CosmeticGUI.COSMETIC_PLAYER_INV_SLOT) return
        if (event.state != PlayerFishEvent.State.REEL_IN) return

        val cooldown = SimpleCooldown()
        if (cooldown.check(player)) {
            val playerLocation = player.location
            val hookLocation = event.hook.location
            val change = hookLocation.subtract(playerLocation)
            player.velocity = change.toVector().multiply(0.3)

            cooldown.setCooldown(event.player, 5)
        } else {
            player.sendMessage("Item Ability is not ready yet")
        }
    }
}

class SnowWand : CosmeticDataInventory(11, CosmeticData.Gadgets.SnowWand), Listener {

}

class FireworkBow(private val plugin: Plugin) : CosmeticDataInventory(12, CosmeticData.Gadgets.FireworkBow), Listener {

    @EventHandler
    fun onBowShoot(event: EntityShootBowEvent) {
        val shooter = event.entity as? Player ?: return

        // Vérifier si l'objet utilisé est dans le slot cosmétique
        if (shooter.inventory.heldItemSlot != CosmeticGUI.COSMETIC_PLAYER_INV_SLOT) {
            shooter.sendMessage("Slot incorrect")
            return
        }

        val arrow = event.projectile as? Arrow ?: run {
            shooter.sendMessage("Projectile n'est pas une flèche!")
            return
        }

        // Générer des couleurs aléatoires
        val randomColor1 = getRandomColor()
        val randomColor2 = getRandomColor()
        val randomColor3 = getRandomColor()

        // Créer un effet de feu d'artifice avec des couleurs aléatoires
        val fireworkEffect = createRandomFireworkEffect(randomColor1, randomColor2, randomColor3)

        // Planifier une tâche pour créer le feu d'artifice après un délai
        object : BukkitRunnable() {
            override fun run() {
                // Créer et lancer un feu d'artifice à l'emplacement de la flèche
                val firework = arrow.world.spawn(arrow.location, Firework::class.java)
                val meta = firework.fireworkMeta
                meta.addEffect(fireworkEffect)
                meta.power = 1
                firework.fireworkMeta = meta

                shooter.sendMessage("Feu d'artifice lancé")
                arrow.remove()
            }
        }.runTaskLater(plugin, 40L) // Ajuster le délai (en ticks) si nécessaire
    }

    // Fonction pour générer une couleur aléatoire
    private fun getRandomColor(): Color {
        val random = ThreadLocalRandom.current()
        return Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    // Fonction pour créer un effet de feu d'artifice aléatoire
    private fun createRandomFireworkEffect(color1: Color, color2: Color, color3: Color): FireworkEffect {
        val types = arrayOf(
            FireworkEffect.Type.BALL,
            FireworkEffect.Type.BALL_LARGE,
            FireworkEffect.Type.STAR,
            FireworkEffect.Type.CREEPER,
            FireworkEffect.Type.BURST
        )

        val randomType = types.random()

        // Personnaliser l'effet de feu d'artifice avec des couleurs aléatoires
        return FireworkEffect.builder()
            .flicker(false)
            .trail(true)
            .with(randomType)
            .withColor(color1, color2, color3)
            .build()
    }
}

class EnderButt(private val plugin: Plugin) : CosmeticDataInventory(13, CosmeticData.Gadgets.EnderButt), Listener {

    private val launchedPearls: MutableMap<Player, EnderPearl> = mutableMapOf()

    @EventHandler
    fun onPearlLaunch(event: ProjectileLaunchEvent) {
        if (event.entityType == EntityType.ENDER_PEARL && event.entity.shooter is Player) {
            val player = event.entity.shooter as Player
            launchedPearls[player] = event.entity as EnderPearl
            event.entity.addPassenger(player)
        }
    }

    @EventHandler
    fun onPearlHit(event: ProjectileHitEvent) {
        if (event.entityType == EntityType.ENDER_PEARL && event.entity.shooter is Player) {
            val player = event.entity.shooter as Player
            val pearl = launchedPearls.remove(player)
            if (pearl != null && pearl == event.entity) {
                player.leaveVehicle()
            }
        }
    }

    @EventHandler
    fun onPearlThrow(event: PlayerInteractEvent) {
        if (event.item?.type != Material.ENDER_PEARL || event.action != org.bukkit.event.block.Action.RIGHT_CLICK_AIR) return

        val player = event.player

        if (launchedPearls.containsKey(player)) {
            return
        }

        val pearl = player.launchProjectile(EnderPearl::class.java)
        pearl.addPassenger(player)
        launchedPearls[player] = pearl
    }

    @EventHandler
    fun onPlayerSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        if (event.isSneaking) {
            val pearl = launchedPearls.remove(player)
            if (pearl != null) {
                player.leaveVehicle()
                pearl.remove()
            }
        }
    }
}

class KnockbackStick : CosmeticDataInventory(14, CosmeticData.Gadgets.KnockbackStick), Listener {

    @EventHandler
    fun onStickUse(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val player = event.player

        // Verify if the item used is in the cosmetic slot
        if (player.inventory.heldItemSlot != CosmeticGUI.COSMETIC_PLAYER_INV_SLOT) return

        // Apply knockback effect to nearby entities
        val entities = player.getNearbyEntities(5.0, 2.0, 5.0)
        for (entity in entities) {
            if (entity is LivingEntity) {
                val direction = entity.location.toVector().subtract(player.location.toVector()).normalize()
                entity.velocity = direction.multiply(2.5)
            }
        }
    }
}
