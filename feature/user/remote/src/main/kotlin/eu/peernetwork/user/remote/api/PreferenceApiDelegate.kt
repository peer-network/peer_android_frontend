package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.PreferenceApi
import eu.peernetwork.user.domain.exception.AccountNotFoundException
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.remote.mapper.mapFromDomain
import eu.peernetwork.user.remote.mapper.mapToDomain
import eu.peernetwork.user.remote.mapper.mapToOnboardingType
import protected.eu.peernetwork.user.remote.PreferenceQuery
import protected.eu.peernetwork.user.remote.UpdateUserPreferencesMutation
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

    override suspend fun set(preference: Preference) {
        val mutation = UpdateUserPreferencesMutation(
            mode = Optional.present(preference.mode.mapFromDomain()),
            flag = Optional.present(preference.flags.map {
                it.mapToOnboardingType()
            })
        )
        val response = client().mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().updateUserPreferences
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
