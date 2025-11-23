package eu.peernetwork.ads.ui.usecase

import eu.peernetwork.ads.domain.usecase.AdsUsecase
import eu.peernetwork.ads.ui.mapper.mapFromDomain
import eu.peernetwork.ads.ui.model.UiCampaign
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class AdsItemUsecase @Inject constructor(
    private val usecase: AdsUsecase
) : ParameterizedSuspendableUseCase<String, UiCampaign> {
    override suspend fun invoke(param: String): UiCampaign {
        return usecase(param).mapFromDomain()
    }
}
