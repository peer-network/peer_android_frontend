package eu.peernetwork.ads.domain.usecase

import eu.peernetwork.ads.domain.model.AdsList
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.repository.AdvertiserRepository
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class AdsListingUsecase @Inject constructor(
    private val repository: AdvertiserRepository
) : ParameterizedSuspendableUseCase<AdsListingUsecase.Parameter, AdsList> {
    override suspend fun invoke(param: Parameter): AdsList {
        return repository.getAll(param.filter, param.page)
    }

    data class Parameter(
        val filter: Filter = Filter(),
        val page: Pageable
    )
}
