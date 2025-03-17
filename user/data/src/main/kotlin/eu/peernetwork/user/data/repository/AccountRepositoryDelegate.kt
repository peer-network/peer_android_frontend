package eu.peernetwork.user.data.repository

import eu.peernetwork.core.common.exception.AuthorizationException
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.data.provider.SettingsProvider
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.AccountDetail
import eu.peernetwork.user.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryDelegate @Inject constructor(
    private val api: AccountApi,
    private val provider: SettingsProvider
) : AccountRepository {
    override suspend fun get(id: String): Account {
        return api.get(id)
    }

    override suspend fun register(detail: AccountDetail): String {
        return api.register(detail)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun update(properties: Map<String, Any>) {
        for (entry in properties) {
            val instance = provider.get(entry.key)
            if (instance is SettingsApi.Updatable<*>) {
                (instance as? SettingsApi.Updatable<Any>)?.invoke(entry.value)
            } else {
                throw AuthorizationException(entry.key)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun update(properties: Map<String, Any>, password: String) {
        for (entry in properties) {
            val instance = provider.get(entry.key)
            if (instance is SettingsApi.SecureUpdatable<*>) {
                (instance as? SettingsApi.SecureUpdatable<Any>)?.invoke(entry.value, password)
            } else {
                (instance as? SettingsApi.Updatable<Any>)?.invoke(entry.value)
            }
        }
    }

    override suspend fun changePassword(old: String, new: String) {
        return api.changePassword(old, new)
    }

    override suspend fun activate(code: String) {
        return api.activate(code)
    }

    override suspend fun delete(password: String) {
        return api.delete(password)
    }
}
