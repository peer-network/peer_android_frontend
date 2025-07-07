package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.model.Chat
import eu.peernetwork.messaging.domain.repository.ChatRepository
import javax.inject.Inject

class UpdateChatUsecase @Inject constructor(
    private val repository: ChatRepository
): ParameterizedSuspendableUseCase<UpdateChatUsecase.UpdateChatParams, Chat> {
    override suspend fun invoke(param: UpdateChatParams): Chat {
        return repository.update(
            chatid = param.chatid,
            name = param.name,
            image = param.image
        )
    }

    data class UpdateChatParams(
        val chatid: String,
        val name: String,
        val image: String?=null
    )
}