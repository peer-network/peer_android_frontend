package eu.peernetwork.messaging.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.messaging.data.api.MessageApi
import eu.peernetwork.messaging.domain.exception.ChatException
import eu.peernetwork.messaging.domain.model.Message
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import messaging.eu.peernetwork.messaging.remote.DeleteChatMessageMutation
import messaging.eu.peernetwork.messaging.remote.GetChatMessagesQuery
import messaging.eu.peernetwork.messaging.remote.SendChatMessageMutation
import javax.inject.Inject

class MessageApiDelegate @Inject constructor(
    private val client: RequestClient
): MessageApi {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun get(chatId: String): Flow<List<Message>> {
        return MutableSharedFlow<Unit>(replay = 0)
            .onStart { emit(Unit) }
            .flatMapLatest {
                flow {
                    emit(handleGet(chatId))
                }
            }
    }

    private suspend fun handleGet(chatId: String): List<Message> {
        val query = GetChatMessagesQuery(chatId = chatId)
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listChatMessages
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.filterNotNull()?.map { msg ->
            Message(
                messId = msg.messid?.toInt() ?: -1,
                chatId = msg.chatid.orEmpty(),
                userId = msg.userid.orEmpty(),
                content = msg.content.orEmpty(),
                createdAt = msg.createdat.toString()
            )
        } ?: emptyList()
    }

    override suspend fun send(chatId: String, content: String): Message {
        val mutation = SendChatMessageMutation(
            chatid = chatId,
            content = content
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().sendChatMessage
        response.assertOrThrow(data.status, data.ResponseCode)
        val msg = data.affectedRows?.firstOrNull() ?: throw ChatException()

        return Message(
            messId = msg.messid?.toInt()!!,
            chatId = msg.chatid.orEmpty(),
            userId = msg.userid.orEmpty(),
            content = msg.content.orEmpty(),
            createdAt = msg.createdat.toString()
        )
    }

    override suspend fun delete(chatId: String, messId: Int) {
        val mutation = DeleteChatMessageMutation(
            chatid = chatId,
            messid = messId
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().deleteChatMessage
        response.assertOrThrow(data.status, data.ResponseCode)
    }

}