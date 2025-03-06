package eu.peernetwork.core.remote.extension

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.core.remote.exception.UndefinedResponseException

fun<D : Operation.Data> ApolloResponse<D>.getResponse(): D {
    if (hasErrors()) {
        throw NetworkException(errors?.first()?.message)
    }
    return data ?: throw UndefinedResponseException()
}
