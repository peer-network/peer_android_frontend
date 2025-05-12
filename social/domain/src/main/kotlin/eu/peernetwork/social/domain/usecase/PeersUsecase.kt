package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.repository.FollowRepository
import javax.inject.Inject

class PeersUsecase @Inject constructor(
    private val repository: FollowRepository
) : ParameterizedSuspendableUseCase<PeersUsecase.Params, Page<Member>> {
    override suspend fun invoke(param: Params): Page<Member> {
        return repository.friends(param.pageable)
    }

    data class Params(
        val pageable: Pageable
    )
}