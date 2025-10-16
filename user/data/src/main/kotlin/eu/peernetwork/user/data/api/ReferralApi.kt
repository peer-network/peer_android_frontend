package eu.peernetwork.user.data.api

import eu.peernetwork.user.domain.model.User

interface ReferralApi {
    suspend fun get(): String

    suspend fun get(code: String): User.Profile
}
