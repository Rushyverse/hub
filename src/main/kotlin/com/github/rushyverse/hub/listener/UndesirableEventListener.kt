package com.github.rushyverse.hub.listener

import com.github.rushyverse.api.extension.event.cancel
import com.github.rushyverse.api.extension.event.cancelIf
import com.github.rushyverse.hub.Hub
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.weather.WeatherChangeEvent

class UndesirableEventListener(
    override val plugin: Hub
) : ListenerHub(plugin) {

    @EventHandler
    fun onWeatherChange(event: WeatherChangeEvent) = event.cancelIf { event.toWeatherState() }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) = event.cancel()

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) = event.cancelIf { entity.world == world }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) = event.cancelIf { event.block.world == world  || isNotAllowed(event.player)}

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) = event.cancelIf { event.block.world == world  || isNotAllowed(event.player)}

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) = event.cancelIf { event.player.world == world  || isNotAllowed(event.player)}

    @EventHandler
    fun onPickupItem(event: PlayerAttemptPickupItemEvent) = event.cancelIf { event.player.world == world  || isNotAllowed(event.player)}

    @EventHandler
    fun onSwapHandItem(event: PlayerSwapHandItemsEvent) = event.cancelIf { event.player.world == world  || isNotAllowed(event.player)}
}