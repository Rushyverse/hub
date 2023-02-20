package com.github.rushyverse.hub.inventories.player

import com.github.rushyverse.api.extension.*
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.hub.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.inventories.IMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.time.ZoneId
import java.util.*

class LoyaltyProgramMenu(
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    private val player: Player
) : IMenu {

    companion object {
        private val CLAIMED_MATERIAL = Material.GREEN_WOOL
        private val CLAIMABLE_MATERIAL = Material.PURPLE_WOOL
        private val NOT_CLAIMABLE_MATERIAL = Material.WHITE_WOOL
        private val DAY_PERIOD = 7;
        private val START_SLOT = 10;
    }

    private var playerLevel = 0

    override suspend fun build(): Inventory {

        val timeZone = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"))
        val calendar = Calendar.getInstance(timeZone, SupportedLanguage.FRENCH.locale)

         calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)

        calendar.firstDayOfWeek = Calendar.MONDAY
        val dayNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1

        val inv = Inventory(
            InventoryType.CHEST_4_ROW,
            translationsProvider.translate("loyalty_menu_title", locale, BUNDLE_HUB, arrayOf(dayNumber))
        )

        // DAY 1 - LEVEL 0
        playerLevel = 1
        for (loyaltyDay in 1..DAY_PERIOD) {
            val currentSlot = START_SLOT + loyaltyDay - 1;

            val item = buildLoyaltyItem(loyaltyDay, dayNumber, playerLevel);
            val material = item.material()

            inv.setItemStackSuspend(currentSlot, item) { player,_,_,_ ->

                if (material == CLAIMABLE_MATERIAL) {

                    player.sendMessage(Component.text(
                        translationsProvider.translate("loyalty_claimed_message", locale, BUNDLE_HUB),
                        NamedTextColor.GREEN
                    ))


                }
            }
        }

        inv.setCloseButton(31)

        playerLevel += 1
        return inv
    }

    fun buildLoyaltyItem(rewardDay:Int, dayNumber:Int, loyaltyLevel:Int) : ItemStack {

        val claimed = loyaltyLevel >= rewardDay
        val claimable = loyaltyLevel == (rewardDay -1) && dayNumber >= rewardDay

        val material = if (claimed) CLAIMED_MATERIAL
                                else if (claimable) CLAIMABLE_MATERIAL
                                else NOT_CLAIMABLE_MATERIAL

        val loreType = if (claimed) "claimed"
                                else if (claimable) "claimable"
                                else "not_claimable"

        val item = ItemStack
            .builder(material)
            .displayName(Component.text(
                    translationsProvider.translate("loyalty_day_item", locale, BUNDLE_HUB, arrayOf(rewardDay)),
                    NamedTextColor.WHITE
                )
                .withoutItalic()
                .withBold()
            )
            .formattedLore(
                translationsProvider.translate("loyalty_day_item_${loreType}_lore", locale, BUNDLE_HUB, arrayOf(dayNumber)),
                30
            )
            .build();

        return item;
    }

}