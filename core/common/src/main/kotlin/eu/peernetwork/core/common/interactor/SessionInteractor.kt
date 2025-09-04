package eu.peernetwork.core.common.interactor

interface SessionInteractor {
    suspend fun principal(): String

    suspend fun mode(): String
}
