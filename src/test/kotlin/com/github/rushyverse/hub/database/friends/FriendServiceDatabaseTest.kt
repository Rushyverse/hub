package com.github.rushyverse.hub.database.friends

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FriendServiceDatabaseTest {

    private lateinit var service: FriendServiceDatabase

    @BeforeTest
    fun onBefore() = runBlocking {
        service = FriendServiceDatabase()
    }

    @AfterTest
    fun onAfter() = runBlocking {

    }

    @Test
    fun `should add friend`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        assertTrue { service.addFriend(uuid1, uuid2) }
    }

    @Test
    fun `should add friends`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val uuid3 = UUID.randomUUID()
        val friends = listOf(uuid2, uuid3)

        assertTrue { service.addFriends(uuid1, friends) }
    }

    @Test
    fun `should add pending friend`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        assertTrue { service.addPendingFriend(uuid1, uuid2) }
    }

    @Test
    fun `should add pending friends`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val uuid3 = UUID.randomUUID()
        val friends = listOf(uuid2, uuid3)

        assertTrue { service.addPendingFriends(uuid1, friends) }
    }

    @Test
    fun `should get friends`() = runTest {
        val uuid1 = UUID.randomUUID()

        assertNotNull(service.getFriends(uuid1))
    }

    @Test
    fun `should get pending friends`() = runTest {
        val uuid1 = UUID.randomUUID()

        assertNotNull(service.getPendingFriends(uuid1))
    }

    @Test
    fun `should be friend`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        assertTrue { service.isFriend(uuid1, uuid2) }
    }

    @Test
    fun `should be pending friend`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        assertTrue { service.isPendingFriend(uuid1, uuid2) }
    }

    @Test
    fun `should remove friend`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        assertTrue { service.removeFriend(uuid1, uuid2) }
    }

    @Test
    fun `should remove friends`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val uuid3 = UUID.randomUUID()

        val friends = listOf(uuid2, uuid3)

        assertTrue { service.removeFriends(uuid1, friends) }
    }

    @Test
    fun `should remove pending friend`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        assertTrue { service.removePendingFriend(uuid1, uuid2) }
    }

    @Test
    fun `should remove pending friends`() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val uuid3 = UUID.randomUUID()

        val friends = listOf(uuid2, uuid3)

        assertTrue { service.removePendingFriends(uuid1, friends) }
    }
}