package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.interactor.SearchInteractor
import eu.peernetwork.social.domain.model.Member
import javax.inject.Inject

class SearchByUserUsecase @Inject constructor(
    private val interactor: SearchInteractor
) : ParameterizedSuspendableUseCase<SearchByUserUsecase.Parameter, Page<Member>> {
    override suspend fun invoke(param: Parameter): Page<Member> {
        return interactor.findMember(param.username, param.page)
    }

    data class Parameter(
        val username: String,
        val page: Pageable
    )
}
