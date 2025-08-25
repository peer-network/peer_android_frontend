package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.model.Referral
import eu.peernetwork.social.domain.repository.ReferralRepository
import javax.inject.Inject

class ReferralUsecase @Inject constructor(
    private val repository: ReferralRepository
): ParameterizedSuspendableUseCase<ReferralUsecase.Params, Page<Referral>> {
    override suspend fun invoke(param: Params): Page<Referral> {
        return repository.getAll(param.userId, param.pageable)
    }

    data class Params(
        val userId: String,
        val pageable: Pageable
    )
}