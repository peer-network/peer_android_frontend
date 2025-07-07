package eu.peernetwork.app.module.messaging

import dagger.Binds
import dagger.Module
import eu.peernetwork.messaging.data.api.ChatApi
import eu.peernetwork.messaging.data.api.MessageApi
import eu.peernetwork.messaging.data.api.ParticipantsApi
import eu.peernetwork.messaging.remote.api.ChatApiDelegate
import eu.peernetwork.messaging.remote.api.MessageApiDelegate
import eu.peernetwork.messaging.remote.api.ParticipantsApiDelegate

@Module
interface ApiModule {
    @Binds
    fun bindChatApi(delegate: ChatApiDelegate): ChatApi

    @Binds
    fun bindMessageApi(delegate: MessageApiDelegate): MessageApi

    @Binds
    fun bindParticipantsApi(delegate: ParticipantsApiDelegate): ParticipantsApi
}