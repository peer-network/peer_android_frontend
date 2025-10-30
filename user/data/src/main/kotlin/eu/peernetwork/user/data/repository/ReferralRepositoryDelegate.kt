package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.ReferralApi
import eu.peernetwork.user.domain.model.Invite
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.repository.ReferralRepository
import javax.inject.Inject

class ReferralRepositoryDelegate @Inject constructor(
    private val api: ReferralApi
) : ReferralRepository {
    override suspend fun get(): String {
        return api.get()
    }

    override suspend fun get(code: String): User.Profile {
        return api.get(code)
    }

    override suspend fun invitation(): Invite {
        return api.invitation()
    }
}
