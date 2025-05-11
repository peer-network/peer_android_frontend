package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import javax.inject.Inject

class PointUsecase @Inject constructor(
    private val interactor: PointInteractor
) : SuspendableUseCase<List<Point>> {
    override suspend fun invoke(): List<Point> {
        return interactor.refresh()
    }
}
