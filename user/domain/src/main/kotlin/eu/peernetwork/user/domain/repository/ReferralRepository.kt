package eu.peernetwork.user.domain.repository

import eu.peernetwork.user.domain.model.User

interface ReferralRepository {
    suspend fun get(): String

    suspend fun get(code: String): User.Profile
}
