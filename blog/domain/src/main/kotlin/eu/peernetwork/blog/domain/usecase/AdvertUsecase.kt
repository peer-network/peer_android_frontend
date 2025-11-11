package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class AdvertUsecase @Inject constructor(
    private val repository: ContentRepository
) : ParameterizedSuspendableUseCase<AdvertUsecase.Parameter, Page<Content>> {
    override suspend fun invoke(param: Parameter): Page<Content> {
        return repository.getAdverts(
            filter = Filter(
                author = param.author,
                type = param.types,
                criteria = param.criteria,
                category = param.category
            ),
            page = param.page
        )
    }

    data class Parameter(
        val author: String? = null,
        val types: Set<Content.Type>,
        val category: Category = Category.NONE,
        val criteria: Filter.Criteria? = null,
        val page: Pageable
    )
}
