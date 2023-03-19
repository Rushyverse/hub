package com.github.rushyverse

import com.github.rushyverse.api.extension.toText
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.ext.asMiniComponent
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HubScoreboardTest : AbstractTest() {

    @Nested
    inner class Title {

        @ParameterizedTest
        @ValueSource(
            strings = [
                "",
                " ",
                "test",
                "My title",
                "<red>my title</red>",
                "<blue>My <yellow>title",
            ]
        )
        fun `should define title using the configuration`(title: String) {
            val conf = expectedDefaultConfiguration.copy(
                scoreboard = expectedDefaultConfiguration.scoreboard.copy(
                    title = title
                )
            )
            val scoreboardConfig = conf.scoreboard

            val scoreboard = HubScoreboard(
                scoreboardConfig,
                mockk(relaxed = true),
                SupportedLanguage.ENGLISH.locale,
                mockk()
            )

            assertEquals(scoreboard.title, scoreboardConfig.title.asMiniComponent())
        }

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    inner class Lines {

        @Test
        @Order(0)
        fun `scoreboard has more than 0 lines`() = runTest {
            val conf = defaultConfigurationOnAvailablePort()
            assertTrue(conf.scoreboard.lines.isNotEmpty())
        }

        @Test
        @Order(1)
        fun `scoreboard has less than 16 lines`() = runTest {
            val conf = defaultConfigurationOnAvailablePort()
            assertTrue(conf.scoreboard.lines.size < 16)
        }

        @Test
        @Order(2)
        fun `does the lines number matches with the configuration after creation`() = runTest {
            val conf = defaultConfigurationOnAvailablePort()
            val scoreboardConfig = conf.scoreboard
            val confLineSize = scoreboardConfig.lines.size

            val scoreboard = HubScoreboard(
                scoreboardConfig,
                mockk(relaxed = true),
                SupportedLanguage.ENGLISH.locale,
                mockk()
            )

            val scoreboardLineSize = scoreboard.lines.size

            assertEquals(scoreboardLineSize, confLineSize)
        }

        @ParameterizedTest
        @EnumSource(SupportedLanguage::class)
        @Order(3)
        fun `has every line been translated`(language: SupportedLanguage) = runTest {
            val conf = expectedDefaultConfiguration.copy(
                scoreboard = expectedDefaultConfiguration.scoreboard.copy(
                    lines = listOf(
                        "<prestige_translate_name>: IV",
                        "<tokens_translate_name>: 50"
                    )
                )
            )
            val scoreboardConfig = conf.scoreboard
            val locale = language.locale
            val translationsProvider = mockk<TranslationsProvider>(relaxed = true)

            // Setup translation mocks
            every {
                translationsProvider.translate("scoreboard.prestige", locale, HubServer.BUNDLE_HUB)
            } returns "Prestige"
            every {
                translationsProvider.translate("scoreboard.tokens", locale, HubServer.BUNDLE_HUB)
            } returns "Tokens"

            val scoreboard = HubScoreboard(
                scoreboardConfig,
                translationsProvider,
                locale,
                mockk()
            )

            // Verify if the current lines has been translated
            val expectedLines = listOf(
                "Prestige: IV",
                "Tokens: 50"
            )

            assertEquals(expectedLines.size, scoreboard.lines.size)

            scoreboard.lines.reversed().forEachIndexed { index, actualLine ->

                val expectedLine = expectedLines[index]
                val actualLineText = actualLine.content.toText()

                assertEquals(expectedLine, actualLineText)
            }
        }
    }
}