package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.repository.FollowRepository
import javax.inject.Inject

class FollowingsUsecase @Inject constructor(
    private val repository: FollowRepository
) : ParameterizedSuspendableUseCase<FollowingsUsecase.Params, Page<Member>> {
    override suspend fun invoke(param: Params): Page<Member> {
        return repository.following(param.userId, param.pageable)
    }

    data class Params(
        val userId: String,
        val pageable: Pageable
    )
}