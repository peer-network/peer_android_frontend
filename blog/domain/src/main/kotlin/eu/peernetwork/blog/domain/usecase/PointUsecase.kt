package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import javax.inject.Inject

class PointUsecase @Inject constructor(
    private val repository: EngagementRepository
) : SuspendableUseCase<List<Point>> {
    override suspend fun invoke(): List<Point> {
        return repository.points()
    }
}
