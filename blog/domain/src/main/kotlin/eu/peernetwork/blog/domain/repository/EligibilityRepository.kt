package eu.peernetwork.blog.domain.repository

interface EligibilityRepository {
    suspend fun get(): String
}