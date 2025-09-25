package eu.peernetwork.blog.domain.repository

import kotlinx.coroutines.flow.Flow

interface EligibilityRepository {
    fun get(): String?

    suspend fun refresh(): String

    suspend fun clear()
}