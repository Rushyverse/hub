package com.github.rushyverse.hub.scoreboard

import com.github.rushyverse.api.extension.withBold
import com.github.rushyverse.api.extension.withItalic
import com.github.rushyverse.api.translation.SupportedLanguage
import com.github.rushyverse.api.translation.TranslationsProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player
import net.minestom.server.scoreboard.Sidebar

class HubScoreboard(val translationsProvider: TranslationsProvider, val player: Player) :
    Sidebar(Component.text("   Rushyverse   ").withBold().withItalic()) {

    init {
        val playerLocale = SupportedLanguage.ENGLISH.locale

        createLine(ScoreboardLine("emptyLine3", Component.empty(), 5))

        createLine(
            ScoreboardLine(
                "prestige",
                Component.text("Prestige:", NamedTextColor.GOLD)
                    .append(Component.text(" [V]", NamedTextColor.WHITE)),
                4
            )
        )

        createLine(ScoreboardLine("emptyLine2", Component.empty(), 3))

        createLine(
            ScoreboardLine(
                "tokens",
                Component.text("Tokens:", NamedTextColor.YELLOW)
                    .append(Component.text(" 500", NamedTextColor.WHITE)),
                2
            )
        )

        createLine(ScoreboardLine("emptyLine1", Component.empty(), 1))

        createLine(
            ScoreboardLine(
                "ip",
                Component.text("    rushy.space", NamedTextColor.LIGHT_PURPLE).withItalic(),
                0
            )
        )
    }

    public fun update(counter: Int) {
        updateLineContent("players", Component.text("Counter : $counter", NamedTextColor.RED))
    }
}