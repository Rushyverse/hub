package com.github.rushyverse.inventories

import com.github.rushyverse.HubPlayer
import com.github.rushyverse.HubServer
import com.github.rushyverse.api.extension.setItemStack
import com.github.rushyverse.api.extension.withBold
import com.github.rushyverse.api.extension.withoutItalic
import com.github.rushyverse.api.item.InventoryConditionSuspend
import com.github.rushyverse.api.item.asNative
import com.github.rushyverse.api.translation.TranslationsProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.inventory.PlayerInventory
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.inventory.condition.InventoryCondition
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.metadata.PlayerHeadMeta

/**
 * Class created to manage hotbar items (player inventory included).
 * @property translationsProvider The provider to translate item contents.
 */
class HotbarItems(
    private val translationsProvider: TranslationsProvider
) {

    fun giveItems(player: HubPlayer) {
        val inv = player.inventory

        // Currently we use the first element of the pair (item),
        // In the future, when the menus will be created,
        // we will use setItemWithHandler function to make open
        // the menu (currently null) for the player

        inv.setItemStack(3, createCosmeticsMenuItem(player).first)

        inv.setItemStack(4, createMainMenuItem(player).first)
        player.setHeldItemSlot(4)

        inv.setItemStack(5, createStatsMenuItem(player).first)

        inv.setItemStack(20, createSettingsItem(player).first)

        inv.setItemStack(22, createLangItem(player).first)

        inv.setItemStack(24, createSocialItem(player).first)
    }

    companion object {

        fun setItemWithHandler(inv: PlayerInventory, slot: Int, itemWithHandler: Pair<ItemStack, InventoryCondition>) =
            inv.setItemStack(slot, itemWithHandler.first, handler = itemWithHandler.second)

        fun listeners(): EventNode<PlayerEvent> = EventNode
            .type("hotbar-listeners", EventFilter.PLAYER)
            .apply {
                addListener(EventListener.builder(PlayerSwapItemEvent::class.java)
                    .handler { it.isCancelled = !it.player.isCreative }
                    .build()
                )
                addListener(EventListener.builder(InventoryPreClickEvent::class.java)
                    .handler { it.isCancelled = !it.player.isCreative }
                    .build()
                )
                addListener(EventListener.builder(ItemDropEvent::class.java)
                    .handler { it.isCancelled = !it.player.isCreative }
                    .build()
                )
                addListener(EventListener.builder(PlayerUseItemEvent::class.java)
                    .handler { event ->
                        val player = event.player
                        val item = event.itemStack
                        val slot = player.heldSlot.toInt()

                        player.inventory.inventoryConditions.forEach {
                            val result = InventoryConditionResult(item, null)
                            it.accept(player, slot, ClickType.RIGHT_CLICK, result)
                            event.isCancelled = result.isCancel
                        }
                    }
                    .build()
                )
            }
    }

    private fun createCosmeticsMenuItem(player: HubPlayer) = createItemWithHandler(
        player,
        Material.SADDLE,
        "cosmetics_item_name",
        NamedTextColor.BLUE,
        null
    )

    private fun createMainMenuItem(player: HubPlayer) = createItemWithHandler(
        player,
        Material.COMPASS,
        "menu_item_name",
        NamedTextColor.AQUA,
        null
    )

    private fun createStatsMenuItem(
        player: HubPlayer,
    ): Pair<ItemStack, InventoryCondition> {

        val pair = createItemWithHandler(
            player,
            Material.PLAYER_HEAD,
            "stats_item_name",
            NamedTextColor.GOLD,
            null
        )

        return pair.copy(
            pair.first.withMeta(PlayerHeadMeta::class.java) {
                it.skullOwner(player.uuid)
                it.playerSkin(player.skin)
            }
        )
    }

    private fun createSettingsItem(player: HubPlayer) = createItemWithHandler(
        player,
        Material.REPEATER,
        "settings_item_name",
        NamedTextColor.GRAY,
        null,
    )

    private fun createLangItem(player: HubPlayer) = createItemWithHandler(
        player,
        Material.MAGENTA_BANNER,
        "lang_item_name",
        NamedTextColor.LIGHT_PURPLE,
        null
    )

    private fun createSocialItem(player: HubPlayer) = createItemWithHandler(
        player,
        Material.TOTEM_OF_UNDYING,
        "social_item_name",
        NamedTextColor.YELLOW,
        null
    )

    private fun createItemWithHandler(
        player: HubPlayer,
        material: Material,
        translateNameKey: String,
        color: NamedTextColor,
        menuToOpen: IMenu?
    ): Pair<ItemStack, InventoryCondition> {
        val item = ItemStack.builder(material)
            .displayName(
                Component.text(
                    translationsProvider.translate(translateNameKey, player.locale!!, HubServer.BUNDLE_HUB),
                    color
                ).withoutItalic().withBold()
            )
            .build()


        return item to InventoryConditionSuspend { playerClicker: Player, _, _, _ ->
            menuToOpen?.let {
                playerClicker.openInventory(it.build())
            }
        }.asNative()
    }
}