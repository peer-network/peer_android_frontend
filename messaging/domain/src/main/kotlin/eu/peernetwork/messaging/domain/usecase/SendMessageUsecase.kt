package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.model.Message
import eu.peernetwork.messaging.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUsecase @Inject constructor(
    private val repository: MessageRepository
): ParameterizedSuspendableUseCase<SendMessageUsecase.SendMessageParams, Message> {
    override suspend fun invoke(param: SendMessageParams): Message {
        return repository.send(
            chatId = param.chatId,
            content = param.content
        )
    }

    data class SendMessageParams(
        val chatId: String,
        val content: String
    )
}