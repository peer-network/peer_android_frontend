package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedObservableUseCase
import eu.peernetwork.messaging.domain.model.Message
import eu.peernetwork.messaging.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessageUsecase @Inject constructor(
    private val repository: MessageRepository
): ParameterizedObservableUseCase<String, List<Message>> {
    override fun invoke(param: String): Flow<List<Message>> {
        return repository.get(param)
    }
}
