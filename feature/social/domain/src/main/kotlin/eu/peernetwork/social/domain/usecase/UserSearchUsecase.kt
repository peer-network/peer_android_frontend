package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.interactor.SearchInteractor
import eu.peernetwork.social.domain.model.Member
import javax.inject.Inject

class UserSearchUsecase @Inject constructor(
    private val repository: SearchInteractor
) : ParameterizedSuspendableUseCase<UserSearchUsecase.Parameter, Page<Member>> {
    override suspend fun invoke(param: Parameter): Page<Member> {
        return repository.findMember(param.username, param.page)
    }

    data class Parameter(
        val username: String,
        val page: Pageable
    )
}
