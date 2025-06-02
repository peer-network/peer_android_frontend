package eu.peernetwork.app.interactor

interface SettingsInteractor {
    fun set(url: String)

    fun invite(url: String)
}
