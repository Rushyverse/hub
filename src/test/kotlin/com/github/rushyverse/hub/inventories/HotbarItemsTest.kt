package com.github.rushyverse.hub.inventories

import com.github.rushyverse.hub.AbstractTest
import com.github.rushyverse.hub.HubPlayer
import com.github.rushyverse.hub.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.api.extension.withoutDecorations
import com.github.rushyverse.api.translation.ResourceBundleTranslationsProvider
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.api.translation.registerResourceBundleForSupportedLocales
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import net.minestom.testing.Env
import net.minestom.testing.EnvTest
import org.junit.jupiter.api.Nested
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

private const val SLOT_COSMETICS = 3
private const val SLOT_MAIN_MENU = 4
private const val SLOT_STATS = 5
private const val SLOT_SETTINGS = 20
private const val SLOT_LANG = 22
private const val SLOT_SOCIAL = 24

@EnvTest
class HotbarItemsTest : AbstractTest() {

    @Nested
    inner class CreateItems {

        @Nested
        inner class CosmeticsItem {

            @Test
            fun `should material is SADDLE`(env: Env) = runTest {
                assertMaterial(env, SLOT_COSMETICS, Material.SADDLE)
            }

            @Test
            fun `should the name is correctly translated`(env: Env) = runTest {
                assertTranslatedName(env, SLOT_COSMETICS, "cosmetics_item_name")
            }
        }

        @Nested
        inner class MainMenuItem {

            @Test
            fun `should material is COMPASS`(env: Env) = runTest {
                assertMaterial(env, SLOT_MAIN_MENU, Material.COMPASS)
            }

            @Test
            fun `should the name is correctly translated`(env: Env) = runTest {
                assertTranslatedName(env, SLOT_MAIN_MENU, "menu_item_name")
            }
        }

        @Nested
        inner class StatsItem {

            @Test
            fun `should material is PLAYER_HEAD`(env: Env) = runTest {
                assertMaterial(env, SLOT_STATS, Material.PLAYER_HEAD)
            }

            @Test
            fun `should the name is correctly translated`(env: Env) = runTest {
                assertTranslatedName(env, SLOT_STATS, "stats_item_name")
            }
        }

        @Nested
        inner class SettingsItem {

            @Test
            fun `should material is REPEATER`(env: Env) = runTest {
                assertMaterial(env, SLOT_SETTINGS, Material.REPEATER)
            }

            @Test
            fun `should the name is correctly translated`(env: Env) = runTest {
                assertTranslatedName(env, SLOT_SETTINGS, "settings_item_name")
            }
        }

        @Nested
        inner class LangItem {

            @Test
            fun `should material is MAGENTA_BANNER`(env: Env) = runTest {
                assertMaterial(env, SLOT_LANG, Material.MAGENTA_BANNER)
            }

            @Test
            fun `should the name is correctly translated`(env: Env) = runTest {
                assertTranslatedName(env, SLOT_LANG, "lang_item_name")
            }
        }

        @Nested
        inner class SocialItem {

            @Test
            fun `should material is TOTEM_OF_UNDYING`(env: Env) = runTest {
                assertMaterial(env, SLOT_SOCIAL, Material.TOTEM_OF_UNDYING)
            }

            @Test
            fun `should the name is correctly translated`(env: Env) = runTest {
                assertTranslatedName(env, SLOT_SOCIAL, "social_item_name")
            }
        }
    }

    @Nested
    inner class GiveItems {

        @Test
        fun `should set the held slot to the main menu item slot`(env: Env) = runTest {
            val instance = env.createFlatInstance()
            val player = env.createPlayer(instance, Pos.ZERO).toHubPlayer()
            player.locale = SupportedLanguage.ENGLISH.locale

            val translationsProvider = mockk<TranslationsProvider> {
                every { translate(any(), any(), any()) } returns "test"
            }

            HotbarItems(translationsProvider).giveItems(player)

            assertEquals(SLOT_MAIN_MENU, player.heldSlot.toInt())
        }
    }
}

private fun assertMaterial(env: Env, slot: Int, material: Material) {
    val instance = env.createFlatInstance()
    val player = env.createPlayer(instance, Pos.ZERO).toHubPlayer()
    player.locale = SupportedLanguage.ENGLISH.locale

    val translationsProvider = mockk<TranslationsProvider> {
        every { translate(any(), any(), any()) } returns "test"
    }

    HotbarItems(translationsProvider).giveItems(player)

    val inv = player.inventory
    val item = inv.getItemStack(slot)

    assertEquals(material, item.material())
}

private fun assertTranslatedName(env: Env, slot: Int, key: String) {
    val instance = env.createFlatInstance()
    val player = env.createPlayer(instance, Pos.ZERO).toHubPlayer()

    val translationsProvider = buildTranslationsProvider()

    SupportedLanguage.values().forEach { language ->
        player.locale = language.locale

        HotbarItems(translationsProvider).giveItems(player)

        val inv = player.inventory
        val item = inv.getItemStack(slot)

        val translateName = translationsProvider.get(key, language.locale, BUNDLE_HUB)

        val displayName = (item.displayName as TextComponent).removeColors()
        val itemName: String = LegacyComponentSerializer.legacy('&').serialize(displayName)

        assertEquals(translateName, itemName)
    }
}

private fun buildTranslationsProvider() = ResourceBundleTranslationsProvider().apply {
    registerResourceBundleForSupportedLocales(
        BUNDLE_HUB,
        ResourceBundle::getBundle
    )
}

private fun Player.toHubPlayer(): HubPlayer = HubPlayer(uuid, username, playerConnection)

private fun TextComponent.removeColors(): Component {
    val newComponent = color(null).withoutDecorations()
    children().forEach {
        it as TextComponent
        newComponent.append(it.removeColors())
    }
    return newComponent
}