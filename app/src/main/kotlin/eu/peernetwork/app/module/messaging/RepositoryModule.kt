package eu.peernetwork.app.module.messaging

import dagger.Binds
import dagger.Module
import eu.peernetwork.messaging.data.repository.ChatRepositoryDelegate
import eu.peernetwork.messaging.data.repository.MessageRepositoryDelegate
import eu.peernetwork.messaging.data.repository.ParticipantRepositoryDelegate
import eu.peernetwork.messaging.domain.repository.ChatRepository
import eu.peernetwork.messaging.domain.repository.MessageRepository
import eu.peernetwork.messaging.domain.repository.ParticipantRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindChatRepository(delegate: ChatRepositoryDelegate): ChatRepository

    @Binds
    fun bindMessageRepository(delegate: MessageRepositoryDelegate): MessageRepository

    @Binds
    fun bindParticipantRepository(delegate: ParticipantRepositoryDelegate): ParticipantRepository
}