package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.AccountDetail
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryDelegate @Inject constructor(
    private val api: AccountApi
) : AccountRepository {
    override suspend fun get(id: String): Account {
        return api.get(id)
    }

    override suspend fun register(detail: AccountDetail): String {
        return api.register(detail)
    }

    override suspend fun update(properties: Map<String, Any>): Account {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(old: String, new: String) {
        return api.changePassword(old, new)
    }

    override suspend fun delete(password: String) {
        return api.delete(password)
    }
}
