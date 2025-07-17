package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.usecase.ExploreUsecase.Parameter
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class ExploreUsecase @Inject constructor(
    private val repository: ContentRepository
): ParameterizedSuspendableUseCase<Parameter, Page<Content>> {
    override suspend fun invoke(param: Parameter): Page<Content> {
        return repository.getAll(
            filter = Filter(
                author = param.author,
                type = setOf(param.type),
                criteria = param.criteria
            ),
            param.page
        )
    }

    data class Parameter(
        val author: String? = null,
        val type: Content.Type = Content.Type.IMAGE,
        val criteria: Filter.Criteria? = null,
        val page: Pageable
    )
}
