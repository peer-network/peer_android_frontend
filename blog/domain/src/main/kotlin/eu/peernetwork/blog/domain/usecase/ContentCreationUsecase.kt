package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class ContentCreationUsecase @Inject constructor(
    private val interactor: ContentInteractor
) : ParameterizedSuspendableUseCase<Draft, Content> {
    override suspend fun invoke(param: Draft): Content {
        return interactor.create(param)
    }
}
