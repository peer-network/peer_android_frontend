package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Point

interface ResourceApi {
    suspend fun points(): List<Point>

    suspend fun string(path: String): String
}
