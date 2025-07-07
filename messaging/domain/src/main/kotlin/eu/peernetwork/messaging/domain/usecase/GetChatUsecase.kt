package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.model.Chat
import eu.peernetwork.messaging.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatUsecase @Inject constructor(
    private val repository: ChatRepository
): ParameterizedSuspendableUseCase<GetChatUsecase.GetChatParams, Page<Chat>> {
    override suspend fun invoke(param: GetChatParams): Page<Chat> {
        return repository.get(
            chatid = param.chatId,
            pageable = param.pageable
        )
    }

    data class GetChatParams(
        val chatId: String,
        val pageable: Pageable
    )
}