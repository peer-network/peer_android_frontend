package eu.peernetwork.blog.ui.usecase

import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val interactor: ContentInteractor
) : ParameterizedSuspendableUseCase<Draft, UiPost> {
    override suspend fun invoke(param: Draft): UiPost = withContext(dispatcher.io) {
        interactor.create(param).mapFromDomain()
    }
}
