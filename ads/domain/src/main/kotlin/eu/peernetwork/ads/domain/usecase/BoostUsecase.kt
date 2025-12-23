package eu.peernetwork.ads.domain.usecase

import eu.peernetwork.ads.domain.model.Order
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class BoostUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : ParameterizedSuspendableUseCase<String, Order> {
    override suspend fun invoke(param: String): Order {
        return repository.create(param)
    }
}
