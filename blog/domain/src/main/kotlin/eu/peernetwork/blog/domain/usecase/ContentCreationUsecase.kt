package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class ContentCreationUsecase @Inject constructor(
    private val repository: ContentRepository,
    private val interactor: PointInteractor
) : ParameterizedSuspendableUseCase<Draft, Content> {
    override suspend fun invoke(param: Draft): Content {
        return try {
            repository.create(param)
        } finally {
            try {
                interactor.refresh()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }
}
