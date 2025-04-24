package eu.peernetwork.blog.domain.usecase

import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.core.common.usecase.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePointUsecase @Inject constructor(
    private val interactor: EngagementInteractor
) : ObservableUseCase<List<Point>> {
    override fun invoke(): Flow<List<Point>> {
        return interactor.observe()
    }
}
