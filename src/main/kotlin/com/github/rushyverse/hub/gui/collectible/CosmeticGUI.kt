package com.github.rushyverse.hub.gui.collectible

import com.github.rushyverse.api.Plugin
import com.github.rushyverse.api.extension.event.unregister
import com.github.rushyverse.api.extension.registerListener
import com.github.rushyverse.api.gui.ItemStackIndex
import com.github.rushyverse.api.gui.PlayerGUI
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.Translator
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.data.CosmeticData
import com.github.rushyverse.hub.extension.GUIUtils
import com.github.rushyverse.hub.extension.ItemStack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.*

open class CosmeticDataInventory(
    val slot: Int,
    val cosmetic: CosmeticData,
    val onClick: (suspend (ClientHub) -> Unit)? = null
) {
    fun createItem(locale: Locale, translator: Translator): ItemStack {
        return ItemStack(
            type = cosmetic.material,
            name = "shop.${cosmetic.name.lowercase()}.name",
            description = "shop.${cosmetic.name.lowercase()}.desc",
            locale = locale,
            translator = translator
        )
    }
}

abstract class CosmeticGUI(
    protected val plugin: Plugin,
    protected val translator: Translator,
    private val translatableTitle: String
) : PlayerGUI() {

    companion object {

        const val COSMETIC_PLAYER_INV_SLOT = 5

        const val DOUBLE_CHEST_SIZE = 54

    }

    protected abstract val cosmetics: List<CosmeticDataInventory>

    override suspend fun register(): Boolean {
        return super.register().also {
            cosmetics.asSequence()
                .filterIsInstance<Listener>()
                .forEach {
                    plugin.registerListener { it }
                }
        }
    }

    override suspend fun unregister(): Boolean {
        return super.unregister().also {
            cosmetics.asSequence()
                .filterIsInstance<Listener>()
                .forEach(Listener::unregister)
        }
    }

    override suspend fun createInventory(owner: InventoryHolder, client: Client): Inventory {
        val locale = client.lang().locale
        return server.createInventory(owner, DOUBLE_CHEST_SIZE, translator.getComponent(translatableTitle, locale))
    }

    override fun getItems(key: Client, size: Int): Flow<ItemStackIndex> {
        return flow {
            val locale = key.lang().locale

            cosmetics.forEach { data ->
                emit(data.slot to data.createItem(locale, translator))
            }

            emit(48 to GUIUtils.createBackward(locale))
            emit(50 to GUIUtils.createUnequip(locale))
        }
    }

    override suspend fun onClick(
        client: Client,
        clickedInventory: Inventory,
        clickedItem: ItemStack,
        event: InventoryClickEvent
    ) {
        val player = client.player ?: return
        player.closeInventory()

        val itemType = clickedItem.type
        if (itemType == Material.BARRIER) {
            onUnselect(player.inventory, client as ClientHub)
            client.send(translator.getComponent("shop.unequipped", client.lang().locale))
            return
        }

        val itemName = clickedItem.displayName()

        if (isAlreadySelected(player.inventory, clickedItem)) {
            client.send(translator.getComponent("shop.item.already.selected", client.lang().locale).append(itemName))
            return
        }
        val cosmeticData = cosmetics.firstOrNull { it.cosmetic.material == itemType }
        cosmeticData?.let { it.onClick?.invoke(client as ClientHub) }
        setItem(clickedItem, cosmeticData, player.inventory, event)


        client.send(
            translator.getComponent("shop.selected", client.lang().locale).append(itemName)
        )
    }

    suspend fun onClickCosmetic(client: ClientHub, clickedItem: ItemStack): Boolean {
        return cosmetics
            .firstOrNull { it.cosmetic.material == clickedItem.type }
            ?.let { it.onClick?.invoke(client) } != null
    }

    /**
     * The method to give a cosmetic item to the player.
     * @param cosmeticItem ItemStack
     * @param inventory PlayerInventory
     * @param event InventoryClickEvent
     */
    protected abstract fun setItem(
        clickedItem: ItemStack,
        cosmetic: CosmeticDataInventory?,
        player: PlayerInventory,
        event: InventoryClickEvent
    )

    /**
     * Called when the player unselect the current cosmetic.
     * @param inventory PlayerInventory
     */
    protected abstract fun onUnselect(inventory: PlayerInventory, client: ClientHub)

    /**
     * To know if the cosmetic is currently selected for this player.
     * @param inventory PlayerInventory - the inventory of the player.
     * @param cosmeticItem ItemStack
     * @return Boolean
     */
    protected abstract fun isAlreadySelected(inventory: PlayerInventory, cosmeticItem: ItemStack): Boolean
}