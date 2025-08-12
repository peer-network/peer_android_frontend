package eu.peernetwork.app.interactor

interface SettingsInteractor {
    suspend fun setUser(user: String)

    suspend fun setMode(mode: String)
}
