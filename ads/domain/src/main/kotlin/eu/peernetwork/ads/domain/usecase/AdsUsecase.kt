package eu.peernetwork.ads.domain.usecase

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class AdsUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : ParameterizedSuspendableUseCase<String, Ads> {
    override suspend fun invoke(param: String): Ads {
        return repository.get(param)
    }
}
