package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.model.Post
import eu.peernetwork.social.domain.repository.SearchRepository
import javax.inject.Inject

class SearchByTitleUsecase @Inject constructor(
    private val repository: SearchRepository
) : ParameterizedSuspendableUseCase<SearchByTitleUsecase.Parameter, Page<Post>> {
    override suspend fun invoke(param: Parameter): Page<Post> {
        return repository.findPostsByTitle(param.title, param.page)
    }

    data class Parameter(
        val title: String,
        val page: Pageable
    )
}
