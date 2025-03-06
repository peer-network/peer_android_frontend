package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.persistence.ProfilePersistence
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.Profile
import eu.peernetwork.user.domain.repository.AccountRepository

class AccountRepositoryDelegate(
    private val api: AccountApi,
    private val persistence: ProfilePersistence
) : AccountRepository {
    override suspend fun get(id: String): Account {
        return api.get(id)
    }

    override suspend fun register(): String {
        return api.register()
    }

    override suspend fun update(profile: Profile): Account {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(old: String, new: String) {
        return api.changePassword(old, new)
    }

    override suspend fun delete(code: String) {
        return api.delete(code)
    }
}
