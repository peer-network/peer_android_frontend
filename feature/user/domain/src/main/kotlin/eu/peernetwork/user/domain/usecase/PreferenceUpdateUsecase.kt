package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.domain.repository.PreferenceRepository
import javax.inject.Inject

class PreferenceUpdateUsecase @Inject constructor(
    private val repository: PreferenceRepository
) : ParameterizedSuspendableUseCase<Preference, Unit> {
    override suspend fun invoke(param: Preference) {
        return repository.set(param)
    }
}
