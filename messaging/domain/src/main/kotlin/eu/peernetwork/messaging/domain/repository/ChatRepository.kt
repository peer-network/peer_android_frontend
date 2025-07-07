package eu.peernetwork.messaging.domain.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.messaging.domain.model.Chat

interface ChatRepository {
    suspend fun get(
        chatid: String,
        pageable: Pageable
    ): Page<Chat>

    suspend fun list(pageable: Pageable): Page<Chat>

    suspend fun create(
        recipients: List<String>,
        name: String,
        image: String?=null
    ): Chat

    suspend fun update(
        chatid: String,
        name: String,
        image: String?=null
    ): Chat

    suspend fun delete(chatid: String)
}