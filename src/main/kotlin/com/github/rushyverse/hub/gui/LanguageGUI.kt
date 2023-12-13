package com.github.rushyverse.hub.gui

import com.destroystokyo.paper.profile.ProfileProperty
import com.github.rushyverse.api.extension.withoutDecorations
import com.github.rushyverse.api.koin.inject
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.player.language.LanguageManager
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.getComponent
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.client.ClientHub
import com.github.rushyverse.hub.gui.commons.GUI
import com.github.rushyverse.hub.scoreboard.HubScoreboard
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

data class LangHeadData(
    val lang: SupportedLanguage,
    val item: ItemStack
)

class LanguageGUI(
    private val plugin: Hub
) : GUI("menu.lang.title", 9) {

    private val langManager: LanguageManager by inject()

    companion object {
        private val headsData = listOf(
            createHead(
                SupportedLanguage.ENGLISH,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19"
            ),
            createHead(
                SupportedLanguage.FRENCH,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNjlhMDY3ZWUzN2U2MzYzNWNhMWU3MjNiNjc2ZjEzOWRjMmRiZGRmZjk2YmJmZWY5OWQ4YjM1Yzk5NmJjIn19fQ=="
            ),
            createHead(
                SupportedLanguage.SPANISH,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJkNzMwYjZkZGExNmI1ODQ3ODNiNjNkMDgyYTgwMDQ5YjVmYTcwMjI4YWJhNGFlODg0YzJjMWZjMGMzYThiYyJ9fX0="
            ),
            createHead(
                SupportedLanguage.GERMAN,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ=="
            ),
            createHead(
                SupportedLanguage.CHINESE,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5YmMwMzVjZGM4MGYxYWI1ZTExOThmMjlmM2FkM2ZkZDJiNDJkOWE2OWFlYjY0ZGU5OTA2ODE4MDBiOThkYyJ9fX0="
            )
        )

        private fun createHead(lang: SupportedLanguage, texture: String): LangHeadData {
            val langName = lang.displayName
            val head = ItemStack(Material.PLAYER_HEAD)
            val skullMeta = head.itemMeta as SkullMeta

            val languageDisplayNames = mapOf(
                SupportedLanguage.ENGLISH to "English",
                SupportedLanguage.FRENCH to "French",
                SupportedLanguage.SPANISH to "Spanish",
                SupportedLanguage.GERMAN to "German",
                SupportedLanguage.CHINESE to "Chinese"
            )

            val displayName = languageDisplayNames[lang] ?: lang.displayName
            skullMeta.displayName(text(displayName, NamedTextColor.WHITE))


            skullMeta.displayName()
            skullMeta.lore(
                listOf(
                    text("Select $langName language", NamedTextColor.WHITE).withoutDecorations()
                )
            )
            val profile = Bukkit.createProfile(UUID.randomUUID())
            profile.setProperty(ProfileProperty("textures", texture))
            skullMeta.playerProfile = profile
            head.setItemMeta(skullMeta)

            return LangHeadData(lang, head)
        }
    }

    override suspend fun applyItems(client: Client, inv: Inventory) {
        headsData.forEach { inv.addItem(it.item) }
    }


    override suspend fun onClick(client: Client, item: ItemStack, event: InventoryClickEvent) {
        if (item.type != Material.PLAYER_HEAD) {
            return
        }
        val player = client.requirePlayer()
        val dataHead = headsData.firstOrNull { it.item == item } ?: return
        val langClicked = dataHead.lang
        val lang = client.lang()
        val locale = lang.locale

        if (lang == langClicked) {
            player.sendMessage(
                super.translator.getComponent(
                    "lang.already.set",
                    locale,
                    arrayOf(langClicked.displayName)
                )
            )
            return
        }

        langManager.set(player, langClicked)

        player.sendMessage(
            super.translator.getComponent(
                "lang.set.success",
                locale,
                arrayOf(langClicked.displayName)
            )
        )

        client.player?.closeInventory()

        // Update Hotbar
        player.inventory.apply {
            clear()
            plugin.sendHotbarItems(client.lang(), this)
        }

        HubScoreboard.send(client as ClientHub)
    }
}