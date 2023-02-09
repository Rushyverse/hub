package com.github.rushyverse.hub.inventories.social

import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.extension.setItemStack
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.core.data.FriendService
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.hub.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.inventories.IMenu
import io.github.universeproject.kotlinmojangapi.MojangAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.util.*

class SocialMenu(
    private val friendService: FriendService,
    private val mojangAPI: MojangAPI,
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player
) : IMenu {



    override suspend fun build(): Inventory {
        val title = translationsProvider.translate("social_menu_title", locale, BUNDLE_HUB)
        val inv = Inventory(InventoryType.CHEST_1_ROW, title)

        val friendInv = FriendsMenu(friendService, mojangAPI, translationsProvider, locale, player, inv).build()
        inv.setItemStack(0, generateFriendsItem()) { player, _, _, _ ->
            player.openInventory(friendInv)
        }

        val guildInv = GuildMenu(translationsProvider, locale, player, inv).build()
        inv.setItemStack(1, generateGuildsItem()) { player, _, _, _ ->
            player.openInventory(guildInv)
        }

        inv.setCloseButton(8)
        return inv
    }

    private fun generateGuildsItem(): ItemStack = createItem(
        Material.CLOCK,
        translationsProvider.translate("social_menu_guilds_item", locale, BUNDLE_HUB),
        translationsProvider.translate("social_menu_guilds_item_lore", locale, BUNDLE_HUB)
    )

    private fun generateFriendsItem(): ItemStack = createItem(
        Material.WRITTEN_BOOK,
        translationsProvider.translate("social_menu_friends_item", locale, BUNDLE_HUB),
        translationsProvider.translate("social_menu_friends_item_lore", locale, BUNDLE_HUB)
    ).withMeta { it.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES) }

    private fun createItem(material: Material, name: String, description: String): ItemStack {
        return ItemStack.builder(material)
            .displayName(Component.text(name).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
            .lore(Component.text(description, NamedTextColor.GRAY))
            .build()
    }

}