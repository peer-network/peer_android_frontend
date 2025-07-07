package eu.peernetwork.messaging.data.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.messaging.data.api.ChatApi
import eu.peernetwork.messaging.domain.model.Chat
import eu.peernetwork.messaging.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryDelegate @Inject constructor(
    private val api: ChatApi
): ChatRepository {
    override suspend fun get(
        chatid: String,
        pageable: Pageable
    ): Page<Chat> {
        return api.get(
            chatid = chatid,
            pageable = pageable
        )
    }

    override suspend fun list(pageable: Pageable): Page<Chat> {
        return api.list(
            pageable = pageable
        )
    }

    override suspend fun create(
        recipients: List<String>,
        name: String,
        image: String?
    ): Chat {
        return api.create(
            recipients = recipients,
            name = name,
            image = image
        )
    }

    override suspend fun update(
        chatid: String,
        name: String,
        image: String?
    ): Chat {
        return api.update(
            chatid = chatid,
            name = name,
            image = image
        )
    }

    override suspend fun delete(chatid: String) {
        return api.delete(
            chatid = chatid
        )
    }
}