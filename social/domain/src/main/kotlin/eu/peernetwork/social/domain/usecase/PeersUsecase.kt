package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.repository.FollowRepository
import javax.inject.Inject

class PeersUsecase @Inject constructor(
    private val repository: FollowRepository
) : ParameterizedSuspendableUseCase<Pageable, Page<Member>> {
    override suspend fun invoke(param: Pageable): Page<Member> {
        return repository.friends(param)
    }
}
