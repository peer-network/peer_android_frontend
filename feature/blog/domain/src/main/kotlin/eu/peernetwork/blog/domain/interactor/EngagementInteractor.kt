package eu.peernetwork.blog.domain.interactor

import kotlinx.coroutines.flow.SharedFlow

interface EngagementInteractor {
    suspend fun like(id: String)

    suspend fun dislike(id: String)

    suspend fun comment(id: String)

    suspend fun view(id: String)

    fun observe(): SharedFlow<Map<String, Reaction>>

    suspend fun clear()

    data class Reaction(
        val like: Boolean?,
        val dislike: Boolean?,
        val viewed: Boolean?,
        val commented: Int?
    )
}
