package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.repository.ParticipantRepository
import javax.inject.Inject

class RemoveParticipantUsecase @Inject constructor(
    private val repository: ParticipantRepository
): ParameterizedSuspendableUseCase<RemoveParticipantUsecase.RemoveParticipantParams, Unit> {
    override suspend fun invoke(param: RemoveParticipantParams) {
        repository.remove(
            chatId = param.chatId,
            recipients = param.recipients
        )
    }

    data class RemoveParticipantParams(
        val chatId: String,
        val recipients: List<String>
    )
}