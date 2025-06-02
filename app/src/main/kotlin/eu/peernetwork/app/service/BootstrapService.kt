package eu.peernetwork.app.service

interface BootstrapService {
    suspend fun initialize()
}