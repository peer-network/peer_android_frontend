package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteChatUsecase @Inject constructor(
    private val repository: ChatRepository
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        repository.delete(param)
    }
}
