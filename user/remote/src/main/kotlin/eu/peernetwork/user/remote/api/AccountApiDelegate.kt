package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.remote.extension.getError
import eu.peernetwork.core.remote.extension.getResponse
import eu.peernetwork.core.remote.extension.mapToDomain
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.domain.exception.AccountNotFoundException
import eu.peernetwork.user.domain.exception.UserRegistrationException
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.AccountDetail
import eu.peernetwork.user.remote.mapper.mapToDomain
import protected.eu.peernetwork.user.remote.DeleteAccountMutation
import `protected`.eu.peernetwork.user.remote.ProfileQuery
import protected.eu.peernetwork.user.remote.UpdatePasswordMutation
import public.eu.peernetwork.user.remote.RegisterMutation
import public.eu.peernetwork.user.remote.VerifiedAccountMutation
import javax.inject.Inject

class AccountApiDelegate @Inject constructor(
    private val client: ApolloClient
) : AccountApi {
    override suspend fun get(id: String): Account {
        val query = ProfileQuery(Optional.present(id))
        val response = client.query(query).execute()
        val data = response.getResponse().profile
        val error = response.operation.getError(data.status?.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
        return data.affectedRows?.mapToDomain() ?: throw AccountNotFoundException()
    }

    override suspend fun register(detail: AccountDetail): String {
        val mutation = RegisterMutation(
            email = detail.email,
            username = detail.username,
            password = detail.password
        )
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().register
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
        return data.userid ?: throw UserRegistrationException()
    }

    override suspend fun changePassword(old: String, new: String) {
        val mutation = UpdatePasswordMutation(
            password = new,
            expassword = old
        )
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().updatePassword
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
    }

    override suspend fun verify(code: String) {
        val mutation = VerifiedAccountMutation(code)
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().verifiedAccount
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
    }

    override suspend fun delete(password: String) {
        val mutation = DeleteAccountMutation(password)
        val response = client.mutation(mutation).execute()
        val data = response.getResponse().deleteAccount
        val error = response.operation.getError(data.status.mapToDomain(), data.ResponseCode)
        if (error != null) {
            throw error
        }
    }
}
