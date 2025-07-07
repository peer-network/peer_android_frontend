package eu.peernetwork.messaging.remote.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.messaging.data.api.ChatApi
import eu.peernetwork.messaging.domain.model.Chat
import messaging.eu.peernetwork.messaging.remote.GetChatQuery
import javax.inject.Inject
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.messaging.domain.exception.ChatException
import messaging.eu.peernetwork.messaging.remote.CreateChatMutation
import messaging.eu.peernetwork.messaging.remote.DeleteChatMutation
import messaging.eu.peernetwork.messaging.remote.ListChatsQuery
import messaging.eu.peernetwork.messaging.remote.UpdateChatInformationsMutation

class ChatApiDelegate @Inject constructor(
    private val client: RequestClient
): ChatApi {
    override suspend fun get(chatid: String, pageable: Pageable): Page<Chat> {
        val query = GetChatQuery(
            chatid = chatid,
            offset = Optional.Present(pageable.offset),
            limit = Optional.Present(pageable.limit)
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().getChat
        response.assertOrThrow(data.status, data.ResponseCode)
        val chats = data.affectedRows?.map {
            Chat(
                id = it.id,
                name = it.name,
                image = it.image,
                createdAt = it.createdat.toString()
            )
        } ?: emptyList()
        return Page(
            items = chats,
            count = data.counter,
            offset = pageable.offset
        )
    }

    override suspend fun list(pageable: Pageable): Page<Chat> {
        val query = ListChatsQuery(
            offset = Optional.Present(pageable.offset),
            limit = Optional.Present(pageable.limit),
        )
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().listChats
        response.assertOrThrow(data.status, data.ResponseCode)
        val list = data.affectedRows?.map {
            Chat(
                id = it.id,
                image = it.image,
                name = it.name,
                createdAt = it.createdat.toString(),
            )
        } ?: emptyList()
        return Page(
            items = list,
            count = data.counter,
            offset = pageable.offset
        )
    }

    override suspend fun create(recipients: List<String>, name: String, image: String?): Chat {
        val mutation = CreateChatMutation(
            recipients = recipients,
            name = name,
            image = Optional.presentIfNotNull(image)
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().createChat
        response.assertOrThrow(data.status, data.ResponseCode)
        val createdChatId = data.affectedRows?.chatid ?: throw ChatException()
        val now = System.currentTimeMillis().toString()

        return Chat(
            id = createdChatId,
            name = name,
            image = image,
            createdAt = now
        )
    }

    override suspend fun update(chatid: String, name: String, image: String?): Chat {
        val mutation = UpdateChatInformationsMutation(
            chatid = chatid,
            name = Optional.presentIfNotNull(name),
            image = Optional.presentIfNotNull(image)
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().updateChatInformations
        response.assertOrThrow(data.status, data.ResponseCode)
        val updatedChatId = data.affectedRows?.chatid ?: throw ChatException()
        val now = System.currentTimeMillis().toString()

        return Chat(
            id = updatedChatId,
            name = name,
            image = image,
            createdAt = now
        )
    }

    override suspend fun delete(chatid: String) {
        val mutation = DeleteChatMutation(chatid)
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().deleteChat
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}