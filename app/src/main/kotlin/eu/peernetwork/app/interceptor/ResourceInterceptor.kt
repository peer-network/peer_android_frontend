package eu.peernetwork.app.interceptor

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import eu.peernetwork.app.service.BootstrapService
import eu.peernetwork.core.remote.exception.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ResourceInterceptor @Inject constructor(
    private val bootstrapService: BootstrapService
) : ApolloInterceptor {
    private val mutex = Mutex()

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> = flow {
        if (!bootstrapService.isReady()) {
            mutex.withLock {
                try {
                    bootstrapService.initialize()
                } catch (error: Throwable) {
                    error.printStackTrace()
                    if (error is NetworkException) {
                        throw error
                    }
                }
            }
        }
        emitAll(chain.proceed(request))
    }
}
