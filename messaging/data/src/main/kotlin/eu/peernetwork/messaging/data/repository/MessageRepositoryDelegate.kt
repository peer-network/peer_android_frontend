package eu.peernetwork.messaging.data.repository

import eu.peernetwork.messaging.data.api.MessageApi
import eu.peernetwork.messaging.domain.model.Message
import eu.peernetwork.messaging.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryDelegate @Inject constructor(
    private val api: MessageApi
): MessageRepository {
    override fun get(chatId: String): Flow<List<Message>> {
        return api.get(chatId = chatId)
    }

    override suspend fun send(
        chatId: String,
        content: String
    ): Message {
        return api.send(
            chatId = chatId,
            content = content
        )
    }

    override suspend fun delete(
        chatId: String,
        messId: Int
    ) {
        return api.delete(
            chatId = chatId,
            messId = messId
        )
    }
}