package eu.peernetwork.messaging.data.api

import eu.peernetwork.messaging.domain.model.Participant

interface ParticipantsApi {
    suspend fun get(chatId: String): List<Participant>

    suspend fun add(
        chatId: String,
        recipients: List<String>
    ): List<Participant>

    suspend fun remove(
        chatId: String,
        recipients: List<String>
    )
}