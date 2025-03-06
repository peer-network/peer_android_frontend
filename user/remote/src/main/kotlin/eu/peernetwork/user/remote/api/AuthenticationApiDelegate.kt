package eu.peernetwork.user.remote.api

import eu.peernetwork.user.data.api.AuthenticationApi

class AuthenticationApiDelegate : AuthenticationApi {
    override suspend fun login(email: String, password: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }
}
