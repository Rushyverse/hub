package com.github.rushyverse.hub.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertTrue

class StringExtTest {

    @Nested
    inner class FormatLore {

        @Test
        fun `should return empty component with empty string`() {
            val component = Component.text()
            assertTrue { component.content().isEmpty() }
        }

        @Test
        fun `should return one component with the string if less than 30 characters`() {
            TODO()
        }

        @Test
        fun `should return several components with the split string if he's one word greater than 30 characters`() {
            TODO()
        }

        @Test
        fun `should return several components with the split string with jump line is on space`() {
            TODO()
        }
    }
}