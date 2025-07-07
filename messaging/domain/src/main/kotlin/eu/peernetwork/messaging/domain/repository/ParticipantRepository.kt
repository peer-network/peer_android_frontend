package eu.peernetwork.messaging.domain.repository

import eu.peernetwork.messaging.domain.model.Participant

interface ParticipantRepository {
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