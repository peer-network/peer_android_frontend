package eu.peernetwork.core.common.interactor

interface NotificationInteractor {
    suspend fun send(to: String, action: String, message: String)
}
