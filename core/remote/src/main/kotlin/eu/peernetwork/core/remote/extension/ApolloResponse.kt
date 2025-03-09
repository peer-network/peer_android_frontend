package eu.peernetwork.core.remote.extension

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.common.exception.BusinessException
import eu.peernetwork.core.common.exception.UnknownBusinessException
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.core.remote.exception.UndefinedResponseException
import eu.peernetwork.core.remote.model.Status

fun<D : Operation.Data> ApolloResponse<D>.getResponse(): D {
    if (hasErrors()) {
        throw NetworkException(errors?.first()?.message)
    }
    return data ?: throw UndefinedResponseException()
}

fun<D : Operation.Data> ApolloResponse<D>.assertOrThrow(status: String?, message: String?) {
    if (status?.mapToDomain() != Status.SUCCESS) {
        throw message?.let { BusinessException(operation.name(), it) }
            ?: UnknownBusinessException(operation.name(), status ?: Status.UNKNOWN.value)
    }
}
