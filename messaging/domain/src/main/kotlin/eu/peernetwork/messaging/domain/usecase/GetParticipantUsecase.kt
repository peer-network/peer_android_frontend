package eu.peernetwork.messaging.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.messaging.domain.model.Participant
import eu.peernetwork.messaging.domain.repository.ParticipantRepository
import javax.inject.Inject

class GetParticipantUsecase @Inject constructor(
    private val repository: ParticipantRepository
): ParameterizedSuspendableUseCase<String, List<Participant>> {
    override suspend fun invoke(param: String): List<Participant> {
        return repository.get(param)
    }
}