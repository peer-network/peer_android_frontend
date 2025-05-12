package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.core.common.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePointUsecase @Inject constructor(
    private val interactor: PointInteractor
) : ObservableUseCase<List<Point>> {
    override fun invoke(): Flow<List<Point>> {
        return interactor.observe()
    }
}
