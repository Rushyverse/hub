package com.github.rushyverse

import com.github.rushyverse.HubServer.Companion.BUNDLE_HUB
import com.github.rushyverse.api.translation.TranslationsProvider
import com.github.rushyverse.configuration.ScoreboardConfiguration
import com.github.rushyverse.ext.asMiniComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minestom.server.entity.Player
import net.minestom.server.scoreboard.Sidebar
import java.util.*

infix fun String.replaceBy(replace: String): TagResolver = Placeholder.unparsed(this, replace)

class HubScoreboard(
    private val config: ScoreboardConfiguration,
    private val translationsProvider: TranslationsProvider,
    private val locale: Locale,
    private val player: Player
) : Sidebar(Component.empty()) {

    var title: Component = Component.empty()
        private set

    private var lastLineNumber = 0

    override fun setTitle(title: Component) {
        super.setTitle(title)
        this.title = title
    }

    init {
        update()
    }

    fun update() {
        val tokens = 42;
        val prestige = 0
        val experience = 21
        val onlineFriends = 3

        setTitle(config.title.asMiniComponent())

        val tagResolvers = arrayOf(
            "prestige_translate_name" replaceBy translationsProvider.translate(
                "scoreboard.prestige",
                locale,
                BUNDLE_HUB
            ),
            "prestige_translate_name" replaceBy translationsProvider.translate(
                "scoreboard.prestige",
                locale,
                BUNDLE_HUB
            ),
            "prestige_level_translate_name" replaceBy translationsProvider.translate(
                "prestige.level",
                locale,
                BUNDLE_HUB,
                arrayOf(prestige)
            ),
            "experience" replaceBy "$experience",
            "experience_translate_name" replaceBy translationsProvider.translate(
                "scoreboard.experience",
                locale,
                BUNDLE_HUB,
                arrayOf(experience)
            ),
            "tokens" replaceBy "$tokens",
            "tokens_translate_name" replaceBy translationsProvider.translate(
                "scoreboard.tokens",
                locale,
                BUNDLE_HUB
            ),
            "friends" replaceBy "$onlineFriends",
            "friends_translate_name" replaceBy translationsProvider.translate("scoreboard.friends", locale, BUNDLE_HUB)
        )

        val lines = config.lines.asReversed().mapIndexed { index, content ->
            println("Read config line $index: $content")
            ScoreboardLine(index.toString(), content.asMiniComponent(*tagResolvers), index)
        }
        setLines(lines)
    }

    private fun setLines(newLines: List<ScoreboardLine>) {
        if(this.lines.isEmpty()) {
            newLines.forEach(this::createLine)
        } else  {
            newLines.take(lines.size).forEach {
                updateLineContent(it.id, it.content)
            }
            if(lines.size > newLines.size) {
                this.lines.drop(newLines.size).forEach {
                    this.removeLine(it.id)
                }
            }
            else {
                newLines.drop(lines.size).forEach {
                    this.createLine(it)
                }
            }


        }

    }
}