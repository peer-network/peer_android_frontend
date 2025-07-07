package eu.peernetwork.messaging.data.api

import eu.peernetwork.messaging.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageApi {
    fun get(
        chatId: String
    ): Flow<List<Message>>

    suspend fun send(
        chatId: String,
        content: String
    ): Message

    suspend fun delete(
        chatId: String,
        messId: Int
    )
}