package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.model.Chat
import eu.peernetwork.messaging.domain.repository.ChatRepository
import javax.inject.Inject

class CreateChatUsecase @Inject constructor(
    private val repository: ChatRepository
): ParameterizedSuspendableUseCase<CreateChatUsecase.CreateChatParams, Chat> {
    override suspend fun invoke(param: CreateChatParams): Chat {
        return repository.create(
            recipients = param.recipients,
            name = param.name,
            image = param.image
        )
    }

    data class CreateChatParams(
        val recipients: List<String>,
        val name: String,
        val image: String? = null
    )
}