package eu.peernetwork.messaging.domain.provider

import eu.peernetwork.messaging.domain.repository.ChatRepository
import eu.peernetwork.messaging.domain.repository.MessageRepository
import eu.peernetwork.messaging.domain.repository.ParticipantRepository

interface RepositoryProvider {
    fun chatRepository(): ChatRepository

    fun messageRepository(): MessageRepository

    fun participantRepository(): ParticipantRepository
}
