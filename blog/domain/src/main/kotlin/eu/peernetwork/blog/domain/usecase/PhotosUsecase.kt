package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.usecase.PhotosUsecase.Parameter
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class PhotosUsecase @Inject constructor(
    private val repository: ContentRepository,
    private val interactor: PointInteractor
) : ParameterizedSuspendableUseCase<Parameter, Page<Content>> {
    override suspend fun invoke(param: Parameter): Page<Content> {
        return try {
            repository.getAll(
                filter = Filter(
                    author = param.author,
                    type = setOf(
                        Content.Type.TEXT,
                        Content.Type.IMAGE
                    ),
                    criteria = param.criteria
                ),
                param.page
            )
        } finally {
            try {
                interactor.refresh()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    data class Parameter(
        val author: String? = null,
        val criteria: Criteria? = null,
        val page: Pageable
    )
}
