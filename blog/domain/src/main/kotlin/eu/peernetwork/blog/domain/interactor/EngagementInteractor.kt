package eu.peernetwork.blog.domain.interactor

interface EngagementInteractor {
    suspend fun like(id: String)

    suspend fun dislike(id: String)
}
