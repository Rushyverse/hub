package com.github.rushyverse.hub.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import kotlin.random.Random

enum class ScrollTextType {
    RANDOM,
    WAVE_RIGHT,
    WAVE_LEFT,
    FLASH
}

class ColorTextScroller(
    private val text: String,
    private val titleColor: NamedTextColor,
    private val scrollColor: NamedTextColor,
    private val bold: Boolean = false,
    vararg types: ScrollTextType
) {
    private var offset = 0

    private val scrollTypes = types
    private var currentTypeId = 0
    private val currentType get() = scrollTypes[currentTypeId]

    private var previousColor: NamedTextColor = scrollColor

    fun next(): Component {
        val shiftedText = Component.text()
        for (i in text.indices) {
            val char = text[i]
            val shiftedColor = shiftColor(i, char, offset)
            shiftedText.append(Component.text(char, shiftedColor))
        }
        offset = (offset + 1) % text.length

        if (offset == 0) {
            //currentColor = NamedTextColor.LIGHT_PURPLE // J'ai mis random, tu veux qu'on puisse décider

            updateCurrentType()
        }

        if (bold) {
            shiftedText.decorate(TextDecoration.BOLD)
        }

        return shiftedText.build()
    }

    private fun updateCurrentType() {
        var id = currentTypeId
        if (id == (scrollTypes.size - 1)) {
            id = 0
        } else {
            id++
        }
        currentTypeId = id
    }

    private fun shiftColor(charIndex: Int, char: Char, offset: Int): TextColor {
        return when (currentType) {
            ScrollTextType.RANDOM -> randomColor()
            ScrollTextType.WAVE_RIGHT -> {
                if (charIndex == offset) {
                    scrollColor
                } else titleColor
            }

            ScrollTextType.WAVE_LEFT -> {
                if (charIndex == (text.length - offset))
                    scrollColor
                else titleColor
            }

            ScrollTextType.FLASH -> {
                if (previousColor == NamedTextColor.WHITE) {
                    previousColor = scrollColor
                } else {
                    previousColor = NamedTextColor.WHITE
                }

                previousColor
            }
        }
    }

    private fun randomColor(): TextColor {
        return TextColor.color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }
}