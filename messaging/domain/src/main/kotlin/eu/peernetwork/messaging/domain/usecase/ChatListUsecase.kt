package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.model.Chat
import eu.peernetwork.messaging.domain.repository.ChatRepository
import javax.inject.Inject

class ChatListUsecase @Inject constructor(
    private val repository: ChatRepository
): ParameterizedSuspendableUseCase<ChatListUsecase.ChatListParams,Page<Chat>> {
    override suspend fun invoke(param: ChatListParams): Page<Chat> {
        return repository.list(
            pageable = param.pageable
        )
    }

    data class ChatListParams(val pageable: Pageable)
}