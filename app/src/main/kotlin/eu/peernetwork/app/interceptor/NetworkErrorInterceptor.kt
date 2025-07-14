package eu.peernetwork.app.interceptor

import android.content.Context
import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import eu.peernetwork.app.R
import eu.peernetwork.core.remote.exception.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class NetworkErrorInterceptor @Inject constructor(
    private val context: Context
) : ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> {
        return chain.proceed(request)
            .catch { throwable ->
                when (throwable) {
                    is ApolloNetworkException -> throw NetworkException(
                        message = context.getString(R.string.network_error_message),
                        cause = throwable
                    )
                    else -> throw throwable
                }
            }
    }
}
