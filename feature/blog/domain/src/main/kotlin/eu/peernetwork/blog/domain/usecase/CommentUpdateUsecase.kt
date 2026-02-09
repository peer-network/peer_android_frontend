package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class CommentUpdateUsecase @Inject constructor(
    private val engagementInteractor: EngagementInteractor
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        return engagementInteractor.comment(param)
    }
}
