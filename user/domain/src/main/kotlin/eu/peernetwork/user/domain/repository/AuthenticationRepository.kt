package eu.peernetwork.user.domain.repository

interface AuthenticationRepository {
    suspend fun authenticated(): String

    suspend fun login(email: String, password: String): String

    suspend fun logout()
}
