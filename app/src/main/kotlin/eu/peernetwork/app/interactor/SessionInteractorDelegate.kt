package eu.peernetwork.app.interactor

import eu.peernetwork.core.common.interactor.SessionInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionInteractorDelegate @Inject constructor(): SessionInteractor, SettingsInteractor {
    private lateinit var user: String

    private lateinit var mode: String

    override suspend fun get(): String = user

    override suspend fun mode(): String = mode

    override suspend fun setUser(user: String) {
        this.user = user
    }

    override suspend fun setMode(mode: String) {
        this.mode = mode
    }
}
