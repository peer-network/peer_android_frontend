package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class VideosUsecase @Inject constructor(
    private val repository: ContentRepository,
    private val interactor: PointInteractor
) : ParameterizedSuspendableUseCase<VideosUsecase.Parameter, Page<Content>> {
    override suspend fun invoke(param: Parameter): Page<Content> {
        return try {
            repository.getAll(
                filter = Filter(
                    author = param.author,
                    type = setOf(Content.Type.VIDEO)
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
        val page: Pageable
    )
}
