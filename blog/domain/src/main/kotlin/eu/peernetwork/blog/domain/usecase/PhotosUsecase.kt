package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.usecase.PhotosUsecase.Parameter
import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class PhotosUsecase @Inject constructor(
    private val repository: ContentRepository,
    private val usecase: CategoryUsecase
) : ParameterizedSuspendableUseCase<Parameter, Page<Content>> {
    override suspend fun invoke(param: Parameter): Page<Content> {
        return repository.getAll(
            filter = Filter(
                author = param.author,
                type = usecase(CategoryUsecase.Parameter(
                    types = param.types,
                    category = param.category
                )),
                criteria = param.criteria
            ),
            param.page
        )
    }

    data class Parameter(
        val author: String? = null,
        val types: Set<Content.Type>,
        val category: Category = Category.ALL,
        val criteria: Filter.Criteria? = null,
        val page: Pageable
    )

    companion object {
        val POST = setOf(Content.Type.TEXT, Content.Type.IMAGE)
        val MEDIA = setOf(Content.Type.VIDEO, Content.Type.AUDIO)
        val FEED = setOf(Content.Type.TEXT, Content.Type.IMAGE, Content.Type.VIDEO, Content.Type.AUDIO)
    }
}
