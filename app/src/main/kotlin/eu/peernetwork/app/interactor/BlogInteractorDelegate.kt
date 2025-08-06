package eu.peernetwork.app.interactor

import eu.peernetwork.blog.ui.interactor.BlogInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogInteractorDelegate @Inject constructor(): BlogInteractor, SettingsInteractor {
    private lateinit var user: String

    private lateinit var mode: String

    override suspend fun user(): String = user

    override suspend fun mode(): String = mode

    override suspend fun setUser(user: String) {
        this.user = user
    }

    override suspend fun setMode(mode: String) {
        this.mode = mode
    }
}
