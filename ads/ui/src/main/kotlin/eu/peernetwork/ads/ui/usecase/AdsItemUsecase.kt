package eu.peernetwork.ads.ui.usecase

import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class AdsItemUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : ParameterizedSuspendableUseCase<String, Ads> {
    override suspend fun invoke(param: String): Ads {
        return repository.get(param)
    }
}
