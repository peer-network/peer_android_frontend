package eu.peernetwork.app.interactor

import eu.peernetwork.app.usecase.DeviceUsecase
import eu.peernetwork.core.common.interactor.SessionInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionInteractorDelegate @Inject constructor(
    private val usecase: DeviceUsecase,
): SessionInteractor, SettingsInteractor {
    private lateinit var user: String

    private lateinit var mode: String

    override suspend fun principal(): String = user

    override suspend fun mode(): String = mode

    override suspend fun setUser(user: String) {
        usecase(user)
        this.user = user
    }

    override suspend fun setMode(mode: String) {
        this.mode = mode
    }
}
