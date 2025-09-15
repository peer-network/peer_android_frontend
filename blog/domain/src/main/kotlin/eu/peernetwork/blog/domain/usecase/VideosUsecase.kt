package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class VideosUsecase @Inject constructor(
    private val repository: ContentRepository
) : ParameterizedSuspendableUseCase<VideosUsecase.Parameter, Page<Content>> {
    override suspend fun invoke(param: Parameter): Page<Content> {
        return repository.getAll(
            filter = Filter(
                author = param.author,
                type = setOf(Content.Type.VIDEO),
                criteria = param.criteria,
                category = param.category
            ),
            param.page
        )
    }

    data class Parameter(
        val author: String? = null,
        val category: Category = Category.NONE,
        val criteria: Filter.Criteria? = null,
        val page: Pageable
    )
}
