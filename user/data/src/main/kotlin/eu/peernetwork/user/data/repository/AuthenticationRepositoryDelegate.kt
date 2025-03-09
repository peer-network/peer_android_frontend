package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryDelegate @Inject constructor(
    private val api: AuthenticationApi
) : AuthenticationRepository {
    override suspend fun login(email: String, password: String): String {
        return api.login(email, password)
    }

    override suspend fun refresh(token: String) {
        return api.refresh(token)
    }

    override suspend fun logout() {
        return api.logout()
    }
}
