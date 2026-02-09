package eu.peernetwork.user.data.api

interface ResourceApi {
    suspend fun string(path: String): String
}
