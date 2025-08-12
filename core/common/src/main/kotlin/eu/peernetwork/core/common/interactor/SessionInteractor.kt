package eu.peernetwork.core.common.interactor

interface SessionInteractor {
    suspend fun get(): String

    suspend fun mode(): String
}
