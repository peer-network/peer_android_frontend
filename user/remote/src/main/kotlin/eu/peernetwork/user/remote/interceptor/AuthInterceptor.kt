package eu.peernetwork.user.remote.interceptor

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import com.apollographql.apollo3.network.http.HttpInfo
import eu.peernetwork.user.domain.usecase.ClearTokenUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val usecase: ClearTokenUsecase
) : ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> {
        return chain.proceed(request).onEach { response ->
            if (response.executionContext[HttpInfo]?.statusCode == 401) {
                usecase()
            }
        }
    }
}
