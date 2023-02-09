package com.github.rushyverse.hub.inventories.social

import com.github.rushyverse.api.extension.setCloseButton
import com.github.rushyverse.api.extension.setPreviousButton
import com.github.rushyverse.api.extension.withoutItalic
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.core.data.FriendService
import com.github.rushyverse.hub.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.inventories.IMenu
import io.github.universeproject.kotlinmojangapi.MojangAPI
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
    private val mojangAPI: MojangAPI,
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

        val profileId = mojangAPI.getName(uuid.toString())
        val playerName: String = profileId?.name ?: uuid.toString()

        val connectionStatus: Boolean = false // TODO()

        LoggerFactory.getLogger(this.javaClass)
            .info("BuildFriendHead(): $playerName")

        val headItem = ItemStack.builder(Material.PLAYER_HEAD)
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
            .build()

        val mojangSkin = mojangAPI.getSkin(uuid.toString())
        if (mojangSkin != null) {
            val texture = mojangSkin.getTexturesProperty().value
            val signature = mojangSkin.signature
            val skin = PlayerSkin(mojangSkin.getTexturesProperty().value, mojangSkin.signature)
            val meta = PlayerHeadMeta.Builder().playerSkin(skin)
            meta.skullOwner(uuid)

            headItem.withMeta { meta }
        }
        return headItem
    }
}