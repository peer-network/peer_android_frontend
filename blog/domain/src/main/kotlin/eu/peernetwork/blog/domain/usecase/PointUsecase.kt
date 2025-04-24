package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import javax.inject.Inject

class PointUsecase @Inject constructor(
    private val interactor: EngagementInteractor
) : SuspendableUseCase<List<Point>> {
    override suspend fun invoke(): List<Point> {
        return interactor.refresh()
    }
}
