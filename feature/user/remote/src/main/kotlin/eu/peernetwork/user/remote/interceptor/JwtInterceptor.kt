package eu.peernetwork.user.remote.interceptor

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.user.domain.usecase.ClearTokenUsecase
import eu.peernetwork.user.domain.usecase.RefreshTokenUsecase
import eu.peernetwork.user.domain.usecase.TokenUsecase
import eu.peernetwork.user.remote.mapper.isExpired
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class JwtInterceptor @Inject constructor(
    private val tokenUsecase: TokenUsecase,
    private val usecase: ClearTokenUsecase,
    private val refreshTokenUsecase: RefreshTokenUsecase
) : ApolloInterceptor {
    private val mutex = Mutex()

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> = flow {
        val token = tokenUsecase()
        var jwt = token?.access
        if (token != null && token.isExpired()) {
            mutex.withLock {
                try {
                    jwt = refreshTokenUsecase(token.refresh).access
                } catch (error: Throwable) {
                    error.printStackTrace()
                    if (error !is NetworkException) {
                        usecase()
                    }
                }
            }
        }
        val modifiedRequest = request.newBuilder().apply {
            if (!jwt.isNullOrEmpty()) {
                addHttpHeader(HEADER_AUTHORIZATION, String.format(HEADER_BEARER, jwt))
            }
        }.build()
        emitAll(chain.proceed(modifiedRequest))
    }

    private companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_BEARER = "Bearer %s"
    }
}
