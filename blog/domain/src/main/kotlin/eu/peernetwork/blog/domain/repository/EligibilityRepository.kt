package eu.peernetwork.blog.domain.repository

interface EligibilityRepository {
    fun get(): String?

    suspend fun refresh(): String

    suspend fun clear()
}