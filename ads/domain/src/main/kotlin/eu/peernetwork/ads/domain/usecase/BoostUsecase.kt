package eu.peernetwork.ads.domain.usecase

import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class BoostUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        return repository.create(param)
    }
}
