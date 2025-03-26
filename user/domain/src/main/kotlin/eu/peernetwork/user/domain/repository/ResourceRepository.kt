package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.Point

interface ResourceRepository {
    suspend fun points(): List<Point>

    suspend fun string(path: String): String
}
