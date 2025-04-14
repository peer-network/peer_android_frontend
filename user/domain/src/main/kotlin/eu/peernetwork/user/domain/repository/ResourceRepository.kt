package eu.peernetwork.user.domain.repository

interface ResourceRepository {
    suspend fun string(path: String): String
}
