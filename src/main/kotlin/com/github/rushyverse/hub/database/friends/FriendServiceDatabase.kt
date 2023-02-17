package com.github.rushyverse.hub.database.friends

import com.github.rushyverse.core.data.IFriendDatabaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}

class FriendServiceDatabase() : IFriendDatabaseService {


    override suspend fun addFriend(uuid: UUID, friend: UUID): Boolean {
        logger.info { "$uuid is now friend with $friend" }

        return true;
    }

    override suspend fun addFriends(uuid: UUID, friends: List<UUID>): Boolean {
        friends.forEach {
            addFriend(uuid, it)
        }

        return true;
    }

    override suspend fun addPendingFriend(uuid: UUID, friend: UUID): Boolean {
        logger.info { "$uuid & $friend's relationship is now pending" }
        return true;
    }

    override suspend fun addPendingFriends(uuid: UUID, friends: List<UUID>): Boolean {
        friends.forEach {
            addPendingFriend(uuid, it)
        }

        return true;
    }

    override suspend fun getFriends(uuid: UUID): Flow<UUID> {
        KotlinLogging.logger { }.info { "Get friends of $uuid" }
        return flow {
            emit(UUID.fromString("9cdf711c-293d-4d9a-9dcf-8ed15cc4e0ef")) // Chekaviah
            emit(UUID.fromString("e8666d45-a735-48c7-8fc8-4815bc2ca146")) // frigiel
            emit(UUID.fromString("b4a93b09-e37c-448f-83ba-0eaf510524b5")) // distractic
        }
    }

    override suspend fun getPendingFriends(uuid: UUID): Flow<UUID> {

        return flow { }
    }

    override suspend fun isFriend(uuid: UUID, friend: UUID): Boolean {

        return true
    }

    override suspend fun isPendingFriend(uuid: UUID, friend: UUID): Boolean {

        return true

    }

    override suspend fun removeFriend(uuid: UUID, friend: UUID): Boolean {


        return true
    }

    override suspend fun removeFriends(uuid: UUID, friends: List<UUID>): Boolean {

        return true
    }

    override suspend fun removePendingFriend(uuid: UUID, friend: UUID): Boolean {
        return true
    }

    override suspend fun removePendingFriends(uuid: UUID, friends: List<UUID>): Boolean {
        return true
    }
}