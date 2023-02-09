package com.github.rushyverse.hub.items.hotbar

import com.github.rushyverse.api.extension.withBold
import com.github.rushyverse.api.extension.withoutItalic
import com.github.rushyverse.api.item.InventoryConditionSuspend
import com.github.rushyverse.api.item.asNative
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.core.data.FriendService
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.inventories.game.MainMenu
import com.github.rushyverse.hub.inventories.player.CosmeticsMenu
import com.github.rushyverse.hub.inventories.player.LangMenu
import com.github.rushyverse.hub.inventories.player.ParametersMenu
import com.github.rushyverse.hub.inventories.player.StatsMenu
import com.github.rushyverse.hub.inventories.social.SocialMenu
import io.github.universeproject.kotlinmojangapi.MojangAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.inventory.condition.InventoryCondition
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.metadata.PlayerHeadMeta
import java.util.*

class HotbarItemsManager(
    val friendService: FriendService,
    val mojangAPI: MojangAPI,
    val translationsProvider: TranslationsProvider
) {

    fun createMenuItemWithHandler(locale: Locale): Pair<ItemStack, InventoryCondition> {
        val item = ItemStack.builder(Material.COMPASS)
            .displayName(
                Component.text(
                    translationsProvider.translate("menu_item_name", locale, HubServer.BUNDLE_HUB), NamedTextColor.AQUA
                ).withoutItalic().withBold()
            ).build()

        return item to InventoryConditionSuspend { player: Player, _, _, _ ->
            val menu = MainMenu(translationsProvider, locale).build()
            player.openInventory(menu)
        }.asNative()
    }

    fun createStatsItemWithHandler(locale: Locale, player: Player): Pair<ItemStack, InventoryCondition> {
        val item = ItemStack.builder(Material.PLAYER_HEAD)
            .meta(PlayerHeadMeta::class.java) {
                it.skullOwner(player.uuid)
                it.playerSkin(PlayerSkin.fromUsername(player.username))
                it.displayName(
                    Component.text(
                        translationsProvider.translate("stats_item_name", locale, HubServer.BUNDLE_HUB),
                        NamedTextColor.GREEN
                    ).withoutItalic().withBold()
                )
            }.build()

        return item to InventoryConditionSuspend { playerClick: Player, _, _, _ ->
            val menu = StatsMenu(translationsProvider, locale, playerClick).build()
            player.openInventory(menu)
        }.asNative()
    }

    fun createCosmeticsItemWithHandler(locale: Locale): Pair<ItemStack, InventoryCondition> {
        val item = ItemStack.builder(Material.SADDLE)
            .displayName(
                Component.text(
                    translationsProvider.translate("cosmetics_item_name", locale, HubServer.BUNDLE_HUB),
                    NamedTextColor.LIGHT_PURPLE
                ).withoutItalic().withBold()
            ).build()

        return item to InventoryConditionSuspend { player: Player, _, _, _ ->
            val menu = CosmeticsMenu(translationsProvider, locale, player).build()
            player.openInventory(menu)
        }.asNative()
    }

    fun createParametersItemWithHandler(locale: Locale): Pair<ItemStack, InventoryCondition> {
        val item = ItemStack.builder(Material.REPEATER)
            .displayName(
                Component.text(
                    translationsProvider.translate("parameters_item_name", locale, HubServer.BUNDLE_HUB),
                    NamedTextColor.LIGHT_PURPLE
                ).withoutItalic()
            ).build()

        return item to InventoryConditionSuspend { player: Player, _, _, _ ->
            val menu = ParametersMenu(translationsProvider, locale, player).build()
            player.openInventory(menu)
        }.asNative()
    }

    fun createLangItemWithHandler(locale: Locale): Pair<ItemStack, InventoryCondition> {
        val item = ItemStack.builder(Material.OAK_SIGN)
            .displayName(
                Component.text(
                    translationsProvider.translate("lang_item_name", locale, HubServer.BUNDLE_HUB), NamedTextColor.GOLD
                ).withoutItalic()
            ).build()

        return item to InventoryConditionSuspend { player: Player, _, _, _ ->
            val menu = LangMenu(translationsProvider, locale, player).build()
            player.openInventory(menu)
        }.asNative()
    }

    fun createSocialItemWithHandler(locale: Locale): Pair<ItemStack, InventoryCondition> {
        val item = ItemStack.builder(Material.TOTEM_OF_UNDYING)
            .displayName(
                Component.text(
                    translationsProvider.translate("social_item_name", locale, HubServer.BUNDLE_HUB),
                    NamedTextColor.YELLOW
                ).withoutItalic()
            ).build()

        val pairItemToInvCondition = item to InventoryConditionSuspend { player: Player, _, _, _ ->
            val menu =
                SocialMenu(
                    friendService,
                    mojangAPI,
                    translationsProvider,
                    locale,
                    player
                ).build()
            player.openInventory(menu)
        }.asNative()

        return pairItemToInvCondition
    }
}