package eu.peernetwork.user.remote.interceptor

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import eu.peernetwork.user.domain.usecase.TokenUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JwtInterceptor @Inject constructor(
    private val tokenUsecase: TokenUsecase
) : ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> {
        val token = tokenUsecase()?.access
        val modifiedRequest = request.newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHttpHeader(HEADER_AUTHORIZATION, String.format(HEADER_BEARER, token))
            }
        }.build()
        return chain.proceed(modifiedRequest)
    }

    private companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_BEARER = "Bearer %s"
    }
}
