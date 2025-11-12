package eu.peernetwork.ads.domain.usecase

import eu.peernetwork.ads.domain.model.Metrics
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class MetricsUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : ParameterizedSuspendableUseCase<String, Metrics> {
    override suspend fun invoke(param: String): Metrics {
        return repository.getMetrics(param)
    }
}
