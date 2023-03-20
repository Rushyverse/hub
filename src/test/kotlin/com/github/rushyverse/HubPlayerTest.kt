package com.github.rushyverse

import com.github.rushyverse.api.translation.SupportedLanguage
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.minestom.server.entity.Player
import net.minestom.server.network.player.PlayerConnection
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HubPlayerTest : AbstractTest() {

    @Test
    fun `should be a Player`() {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val player = HubPlayer(uuid, username, playerConnection)

        assertTrue(player is Player)
    }

    @Test
    fun `should constructor assigns variables correctly`() = runTest {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val prestige = 1
        val tokens = 2
        val experience = 3.0
        val friends = setOf(UUID.randomUUID(), UUID.randomUUID())

        val player = HubPlayer(uuid, username, playerConnection, prestige, tokens, experience, friends)

        assertEquals(uuid, player.uuid)
        assertEquals(username, player.username)
        assertEquals(playerConnection, player.playerConnection)
        assertEquals(prestige, player.prestige)
        assertEquals(tokens, player.tokens)
        assertEquals(experience, player.experience)
        assertEquals(friends, player.friends)
    }

    @ParameterizedTest
    @EnumSource(SupportedLanguage::class)
    fun `should set locale`(language: SupportedLanguage) = runTest {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val player = HubPlayer(uuid, username, playerConnection)

        player.setLocale(language)

        assertEquals(language.locale, player.locale)
    }

    @Test
    fun `should set and get tokens`() {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val player = HubPlayer(uuid, username, playerConnection)

        player.tokens = 10

        assertEquals(10, player.tokens)
    }

    @Test
    fun `should set and get prestige`() {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val player = HubPlayer(uuid, username, playerConnection)

        player.prestige = 2

        assertEquals(2, player.prestige)
    }

    @Test
    fun `should set and get experience`() {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val player = HubPlayer(uuid, username, playerConnection)

        player.experience = 50.0

        assertEquals(50.0, player.experience)
    }

    @Test
    fun `should set and get friends`() {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val player = HubPlayer(uuid, username, playerConnection)

        val setOfFriends = setOf(UUID.randomUUID(), UUID.randomUUID())

        player.friends = setOfFriends

        assertEquals(setOfFriends, player.friends)
    }

    @Test
    fun `should set and get guildID`() {
        val uuid = UUID.randomUUID()
        val username = "testPlayer"
        val playerConnection = mockk<PlayerConnection>(relaxed = true)
        val player = HubPlayer(uuid, username, playerConnection)

        val guildID = 1111L

        player.guildID = guildID

        assertEquals(guildID, player.guildID)
    }
}