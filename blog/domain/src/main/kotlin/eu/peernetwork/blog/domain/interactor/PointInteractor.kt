package eu.peernetwork.blog.domain.interactor

import eu.peernetwork.blog.domain.model.Point
import kotlinx.coroutines.flow.SharedFlow

interface PointInteractor {
    suspend fun refresh(): List<Point>

    fun observe(): SharedFlow<List<Point>>
}
