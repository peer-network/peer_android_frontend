package eu.peernetwork.user.remote.api

import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.PreferenceApi
import eu.peernetwork.user.domain.exception.AccountNotFoundException
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.remote.mapper.mapToDomain
import protected.eu.peernetwork.user.remote.PreferenceQuery
import javax.inject.Inject

class PreferenceApiDelegate @Inject constructor(
    private val client: RequestClient
) : PreferenceApi {
    override suspend fun get(): Preference {
        val query = PreferenceQuery()
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().getUserInfo
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.affectedRows?.userPreferences?.mapToDomain() ?: throw AccountNotFoundException()
    }
}
