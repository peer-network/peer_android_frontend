package eu.peernetwork.blog.ui.interactor

interface BlogInteractor {
    suspend fun user(): String

    suspend fun mode(): String
}
