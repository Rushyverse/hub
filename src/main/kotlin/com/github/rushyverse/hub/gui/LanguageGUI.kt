package com.github.rushyverse.hub.gui

import com.destroystokyo.paper.profile.ProfileProperty
import com.github.rushyverse.api.player.Client
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.hub.Hub
import com.github.rushyverse.hub.Hub.Companion.BUNDLE_HUB
import com.github.rushyverse.hub.gui.commons.GUI
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

data class LangHeadData(
    val lang: SupportedLanguage,
    val item: ItemStack
)

class LanguageGUI : GUI("menu.lang.title", 9) {


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

            skullMeta.displayName()
            skullMeta.lore(
                listOf(
                    text("Select $langName language", NamedTextColor.WHITE)
                )
            )
            val profile = Bukkit.createProfile(UUID.randomUUID())
            profile.setProperty(ProfileProperty("textures", texture))
            skullMeta.playerProfile = profile
            head.setItemMeta(skullMeta)

            return LangHeadData(lang, head)
        }
    }

    override fun applyItems(client: Client, inv: Inventory) {
        headsData.forEach { inv.addItem(it.item) }
    }


    override suspend fun onClick(client: Client, item: ItemStack, clickType: ClickType) {
        if (item.type != Material.PLAYER_HEAD) {
            return
        }
        val dataHead = headsData.firstOrNull { it.item == item } ?: return
        val langClicked = dataHead.lang

        if (client.lang == langClicked) {
            client.send(
                Hub.translator.translate(
                    "lang.already.set",
                    client.lang.locale,
                    BUNDLE_HUB,
                    arrayOf(langClicked.displayName)
                )
            )
            return
        }

        client.lang = langClicked
        client.send(
            Hub.translator.translate(
                "lang.set.success",
                client.lang.locale,
                BUNDLE_HUB,
                arrayOf(langClicked.displayName)
            )
        )

        client.player?.closeInventory()

    }
}