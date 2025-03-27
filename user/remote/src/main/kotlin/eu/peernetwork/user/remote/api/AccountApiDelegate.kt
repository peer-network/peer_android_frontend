package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.model.AccountModel
import eu.peernetwork.user.domain.exception.AccountNotFoundException
import eu.peernetwork.user.domain.exception.UserRegistrationException
import eu.peernetwork.user.domain.model.UserDetail
import eu.peernetwork.user.remote.mapper.mapToDomain
import protected.eu.peernetwork.user.remote.DeleteAccountMutation
import `protected`.eu.peernetwork.user.remote.ProfileQuery
import protected.eu.peernetwork.user.remote.UpdatePasswordMutation
import public.eu.peernetwork.user.remote.RegisterMutation
import public.eu.peernetwork.user.remote.VerifiedAccountMutation
import javax.inject.Inject
import javax.inject.Named

class AccountApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: ApolloClient
) : AccountApi {
    override suspend fun get(id: String): AccountModel {
        val query = ProfileQuery(Optional.present(id))
        val response = client.query(query).executeOrThrow()
        val data = response.getOrThrow().profile
        response.assertOrThrow(data.status, data.ResponseCode)
        val account = data.affectedRows?.mapToDomain()
        return account?.copy(
            imageUrl = "$url${account.imageUrl}?q=${System.currentTimeMillis()}"
        ) ?: throw AccountNotFoundException()
    }

    override suspend fun register(detail: UserDetail): String {
        val mutation = RegisterMutation(
            email = detail.email,
            username = detail.username,
            password = detail.password
        )
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().register
        response.assertOrThrow(data.status, data.ResponseCode)
        return data.userid ?: throw UserRegistrationException()
    }

    override suspend fun changePassword(old: String, new: String) {
        val mutation = UpdatePasswordMutation(
            password = new,
            expassword = old
        )
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().updatePassword
        response.assertOrThrow(data.status, data.ResponseCode)
    }

    override suspend fun activate(code: String) {
        val mutation = VerifiedAccountMutation(code)
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().verifiedAccount
        response.assertOrThrow(data.status, data.ResponseCode)
    }

    override suspend fun delete(password: String) {
        val mutation = DeleteAccountMutation(password)
        val response = client.mutation(mutation).executeOrThrow()
        val data = response.getOrThrow().deleteAccount
        response.assertOrThrow(data.status, data.ResponseCode)
    }
}
