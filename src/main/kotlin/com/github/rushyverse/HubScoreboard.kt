package com.github.rushyverse

import com.github.rushyverse.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.configuration.ScoreboardConfiguration
import com.github.rushyverse.ext.asMiniComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minestom.server.entity.Player
import net.minestom.server.scoreboard.Sidebar
import java.util.*

class HubScoreboard(
    private val config: ScoreboardConfiguration,
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    private val player: Player
) : Sidebar(Component.empty()) {

    init {
        val tokens = 42;
        val prestige = 0
        val experience = 21
        val onlineFriends = 3

        setTitle(config.title.asMiniComponent())

        config.lines.reversed().forEachIndexed { index, line ->
            println("Line $index : $line")

            val id = "line_$index"

            val content = line.asMiniComponent(
                Placeholder.unparsed(
                    "prestige_translate_name",
                    translationsProvider.translate("scoreboard.prestige", locale, BUNDLE_HUB)
                ),
                Placeholder.unparsed(
                    "prestige_level_translate_name",
                    translationsProvider.translate("prestige.level", locale, BUNDLE_HUB, arrayOf(prestige))
                ),
                Placeholder.unparsed("experience", "$experience"),
                Placeholder.unparsed(
                    "experience_translate_name",
                    translationsProvider.translate("scoreboard.experience", locale, BUNDLE_HUB, arrayOf(experience))
                ),
                Placeholder.unparsed("tokens", "$tokens"),
                Placeholder.unparsed(
                    "tokens_translate_name",
                    translationsProvider.translate("scoreboard.tokens", locale, BUNDLE_HUB, arrayOf(tokens))
                ),
                Placeholder.unparsed("friends", "$onlineFriends"),
                Placeholder.unparsed(
                    "friends_translate_name",
                    translationsProvider.translate("scoreboard.friends", locale, BUNDLE_HUB)
                ),
            )

            createLine(ScoreboardLine(id, content, index))
        }
    }
}