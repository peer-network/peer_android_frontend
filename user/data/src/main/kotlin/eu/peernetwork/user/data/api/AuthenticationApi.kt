package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Token

interface AuthenticationApi {
    suspend fun authenticated(): String

    suspend fun login(email: String, password: String): String

    suspend fun logout()

    interface Listener {
        suspend fun onAuthenticationChanged(token: Token?)
    }
}
