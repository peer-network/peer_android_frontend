package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.domain.repository.PreferenceRepository
import javax.inject.Inject

class PreferenceUsecase @Inject constructor(
    private val repository: PreferenceRepository
) : SuspendableUseCase<Preference> {
    override suspend fun invoke(): Preference {
        return repository.get()
    }
}
