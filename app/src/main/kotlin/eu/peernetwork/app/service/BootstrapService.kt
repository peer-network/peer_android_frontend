package eu.peernetwork.app.service

interface BootstrapService {
    fun isReady(): Boolean

    suspend fun initialize()
}