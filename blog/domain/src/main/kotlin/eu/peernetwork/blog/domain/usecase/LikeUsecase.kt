package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject

class LikeUsecase @Inject constructor(
    private val repository: EngagementRepository,
    private val interactor: EngagementInteractor
) : ParameterizedSuspendableUseCase<String, Unit> {
    override suspend fun invoke(param: String) {
        return try {
            repository.post(param, Engagement.Content.Like)
        } finally {
            try {
                interactor.refresh()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }
}
