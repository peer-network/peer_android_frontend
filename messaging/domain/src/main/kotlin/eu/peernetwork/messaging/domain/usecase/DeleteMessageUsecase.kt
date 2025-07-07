package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.repository.MessageRepository
import javax.inject.Inject

class DeleteMessageUsecase @Inject constructor(
    private val repository: MessageRepository
): ParameterizedSuspendableUseCase<DeleteMessageUsecase.DeleteMessageParams, Unit> {
    override suspend fun invoke(param: DeleteMessageParams) {
        repository.delete(
            chatId = param.chatId,
            messId = param.messId
        )
    }

    data class DeleteMessageParams(
        val chatId: String,
        val messId: Int
    )
}