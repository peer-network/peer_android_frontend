package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class CommentLikeUsecase @Inject constructor(
    private val repository: EngagementRepository
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        return repository.comment(param, Engagement.Comment.Like)
    }
}