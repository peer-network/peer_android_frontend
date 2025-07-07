package eu.peernetwork.messaging.data.repository

import eu.peernetwork.messaging.data.api.ParticipantsApi
import eu.peernetwork.messaging.domain.model.Participant
import eu.peernetwork.messaging.domain.repository.ParticipantRepository
import javax.inject.Inject

class ParticipantRepositoryDelegate @Inject constructor(
    private val api: ParticipantsApi
): ParticipantRepository {
    override suspend fun get(chatId: String): List<Participant> {
        return api.get(
            chatId = chatId
        )
    }

    override suspend fun add(
        chatId: String,
        recipients: List<String>
    ): List<Participant> {
        return api.add(
            chatId = chatId,
            recipients = recipients
        )
    }

    override suspend fun remove(
        chatId: String,
        recipients: List<String>
    ) {
        return api.remove(
            chatId = chatId,
            recipients = recipients
        )
    }
}