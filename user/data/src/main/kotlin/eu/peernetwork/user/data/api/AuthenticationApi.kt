package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.Token

interface AuthenticationApi {
    suspend fun login(email: String, password: String): String

    interface Listener {
        suspend fun onAuthenticationChanged(token: Token?)
    }
}
