package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.usecase.PhotosUsecase.Parameter
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class PhotosUsecase @Inject constructor(
    private val repository: ContentRepository,
    private val usecase: CategoryUsecase
) : ParameterizedSuspendableUseCase<Parameter, Page<Content>> {
    override suspend fun invoke(param: Parameter): Page<Content> {
        val baseTypes = setOf(Content.Type.TEXT, Content.Type.IMAGE)
        return repository.getAll(
            filter = Filter(
                author = param.author,
                type = usecase(CategoryUsecase.Parameter(baseTypes, param.category)),
                criteria = param.criteria
            ),
            param.page
        )
    }

    data class Parameter(
        val author: String? = null,
        val category: Category = Category.ALL,
        val criteria: Filter.Criteria? = null,
        val page: Pageable
    )
}
