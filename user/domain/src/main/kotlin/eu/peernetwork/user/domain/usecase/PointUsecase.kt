package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.model.Point
import eu.peernetwork.user.domain.repository.ResourceRepository
import javax.inject.Inject

class PointUsecase @Inject constructor(
    private val repository: ResourceRepository
) : SuspendableUseCase<List<Point>> {
    override suspend fun invoke(): List<Point> {
        return repository.points()
    }
}
