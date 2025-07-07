package eu.peernetwork.messaging.domain.repository

import eu.peernetwork.messaging.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun get(chatId: String): Flow<List<Message>>

    suspend fun send(
        chatId: String,
        content: String
    ): Message

    suspend fun delete(
        chatId: String,
        messId: Int
    )
}