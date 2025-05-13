package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import javax.inject.Inject

class EngagementRefreshUsecase @Inject constructor(
    private val interactor: EngagementInteractor
) : SuspendableUseCase<Unit> {
    override suspend fun invoke() {
        return interactor.clear()
    }
}
