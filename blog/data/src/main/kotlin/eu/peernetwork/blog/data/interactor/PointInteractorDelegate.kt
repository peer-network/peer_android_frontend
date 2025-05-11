package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.PointInteractor
import eu.peernetwork.blog.domain.model.Point
import eu.peernetwork.blog.domain.repository.EngagementRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class PointInteractorDelegate @Inject constructor(
    private val repository: EngagementRepository
) : PointInteractor {
    private val mutableState = MutableSharedFlow<List<Point>>(replay = 1)

    init { mutableState.tryEmit(emptyList()) }

    override suspend fun refresh(): List<Point> {
        val points = repository.points()
        mutableState.tryEmit(points)
        return points
    }

    override fun observe(): SharedFlow<List<Point>> = mutableState
}
