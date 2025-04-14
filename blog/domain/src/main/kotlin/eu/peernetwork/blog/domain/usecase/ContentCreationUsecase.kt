package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class ContentCreationUsecase @Inject constructor(
    private val repository: ContentRepository
) : ParameterizedSuspendableUseCase<Draft, Content> {
    override suspend fun invoke(param: Draft): Content {
        return repository.create(param)
    }
}
