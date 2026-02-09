package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryDelegate @Inject constructor(
    private val api: AuthenticationApi
) : AuthenticationRepository {
    override suspend fun authenticated(): String {
        return api.authenticated()
    }

    override suspend fun login(email: String, password: String, remember: Boolean): String {
        return api.login(email, password, remember)
    }

    override suspend fun logout() {
        return api.logout()
    }
}
