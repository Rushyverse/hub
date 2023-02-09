package com.github.rushyverse.hub.inventories.social

import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.extension.setPreviousButton
import com.github.rushyverse.api.extension.withoutItalic
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.core.data.FriendService
import com.github.rushyverse.core.data.MojangService
import com.github.rushyverse.hub.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.inventories.IMenu
import mu.KotlinLogging
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.metadata.PlayerHeadMeta
import org.slf4j.LoggerFactory
import java.util.*

class FriendsMenu(
    private val friendService: FriendService,
    private val mojangService: MojangService,
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    val player: Player,
    private val previousInventory: Inventory? = null
) : IMenu {

    override suspend fun build(): Inventory {
        val title = translationsProvider.translate("friends_menu_title", locale, BUNDLE_HUB)
        val inv = Inventory(InventoryType.CHEST_4_ROW, title)
        val uuid = player.uuid

        friendService.getFriends(uuid).collect {
            KotlinLogging.logger { }.info { "Friend : $it" }
            val itemFriendHead = buildFriendHead(it)
            inv.addItemStack(itemFriendHead)
        }

        previousInventory?.let {
            inv.setPreviousButton(30, it)
        }
        inv.setCloseButton(32)
        return inv
    }

    private suspend fun buildFriendHead(uuid: UUID): ItemStack {
        val profileSkin = mojangService.getSkinById(uuid.toString())


        val playerName: String = (profileSkin?.name ?: uuid.toString())
        val connectionStatus: Boolean = false // TODO()

        LoggerFactory.getLogger(this.javaClass)
            .info("BuildFriendHead(): $playerName")

        val headMeta = PlayerHeadMeta.Builder().skullOwner(uuid)
        if (profileSkin != null) {
            val textures = profileSkin.getTexturesProperty().value
            val signature = profileSkin.signature
            headMeta.playerSkin(PlayerSkin(textures, signature))
        }

        return ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(
                Component.text(playerName).color(NamedTextColor.GREEN).withoutItalic()
            )
            .lore(
                Component.text(
                    translationsProvider.translate(
                        if (connectionStatus) "friends_menu_online" else "friends_menu_offline",
                        locale, BUNDLE_HUB
                    )
                ).color(NamedTextColor.GRAY)
            )
            .meta(headMeta.build())
            .build()
    }
}