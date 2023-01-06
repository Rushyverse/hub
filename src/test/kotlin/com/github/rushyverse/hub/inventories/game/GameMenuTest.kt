package com.github.rushyverse.hub.inventories.game

import com.github.rushyverse.api.translation.ResourceBundleTranslationsProvider
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.hub.HubServer
import com.github.rushyverse.utils.randomString
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.coordinate.Pos
import net.minestom.server.inventory.InventoryType
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.inventory.condition.InventoryConditionResult
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.testing.Env
import net.minestom.testing.EnvTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

@EnvTest
class GameMenuTest {

    @Nested
    inner class TranslatedTitle {

        @Test
        fun `should build the inventory with translated english title`(env: Env) {
            val game = randomString()
            assertTitleTranslateInventory(
                env,
                SupportedLanguage.ENGLISH.locale,
                game,
                "Game menu of $game"
            )
        }

        @Test
        fun `should build the inventory with translated french title`(env: Env) {
            val game = randomString()
            assertTitleTranslateInventory(
                env,
                SupportedLanguage.FRENCH.locale,
                game,
                "Menu du jeu $game"
            )
        }

        private fun assertTitleTranslateInventory(env: Env, locale: Locale, game: String, expectedTitle: String) {
            val menu = createMenuWithEnvInstances(env, locale, game)
            val inventory = menu.build()
            assertEquals(Component.text(expectedTitle), inventory.title)
        }

    }

    @Nested
    inner class InventoryTypeTest {

        @Test
        fun `should be a 6 row chest inventory`(env: Env) {
            val menu = createMenuWithEnvInstances(env)
            val inventory = menu.build()
            assertEquals(InventoryType.CHEST_6_ROW, inventory.inventoryType)
        }
    }

    @Nested
    inner class Items {

        @Nested
        inner class SubmitProposal {

            private val expectedSlot = 6

            @Test
            fun `should be on the slot 6`(env: Env) {
                val menu = createMenuWithEnvInstances(env)
                val inventory = menu.build()
                val item = inventory.getItemStack(expectedSlot)
                assertEquals(createExpectedItem(), item)
            }

            @Test
            fun `should be open the submit proposal book for the player`(env: Env) {
                val game = randomString()
                val menu = createMenuWithEnvInstances(env, game = game)
                val player = menu.player

                val expectedBook = SubmitProposalMenu(player, game).get()
                val playerSpy = spyk(menu.player) {
                    justRun { openBook(any<Book>()) }
                }

                val inventory = menu.build()
                val item = inventory.getItemStack(expectedSlot)
                inventory.inventoryConditions.forEach {
                    val result = InventoryConditionResult(item, null)
                    it.accept(playerSpy, expectedSlot, ClickType.RIGHT_CLICK, result)
                }

                verify(exactly = 1) { playerSpy.openBook(expectedBook) }
            }

            private fun createExpectedItem(): ItemStack {
                val item = ItemStack.of(Material.PAPER)
                    .withDisplayName(
                        Component.text("Proposer une idée").color(NamedTextColor.GREEN)
                    ).withLore(
                        listOf(
                            Component.text("Cliquez pour proposer une idée\nconcernant ce jeu")
                        )
                    )
                return item;
            }
        }


    }

    private fun createMenuWithEnvInstances(
        env: Env,
        locale: Locale = SupportedLanguage.ENGLISH.locale,
        game: String = randomString()
    ): GameMenu {
        val instance = env.createFlatInstance()
        val player = env.createPlayer(instance, Pos.ZERO)

        val translationsProvider = ResourceBundleTranslationsProvider().apply {
            registerResourceBundle(HubServer.BUNDLE_HUB, locale, ResourceBundle::getBundle)
        }
        return GameMenu(translationsProvider, locale, player, game)
    }
}