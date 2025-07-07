package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.model.Participant
import eu.peernetwork.messaging.domain.repository.ParticipantRepository
import javax.inject.Inject

class AddParticipantUsecase @Inject constructor(
    private val repository: ParticipantRepository
): ParameterizedSuspendableUseCase<AddParticipantUsecase.AddParticipantParams, List<Participant>> {
    override suspend fun invoke(param: AddParticipantParams): List<Participant> {
        return repository.add(
            chatId = param.chatId,
            recipients = param.recipients
        )
    }

    data class AddParticipantParams(
        val chatId: String,
        val recipients: List<String>
    )
}