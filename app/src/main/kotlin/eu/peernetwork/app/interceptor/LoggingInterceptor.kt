package eu.peernetwork.app.interceptor

import android.util.Log
import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoggingInterceptor @Inject constructor(
    private val gson: Gson
) : ApolloInterceptor {
    private val tag = LoggingInterceptor::class.java.simpleName

    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> {
        Log.d(tag, request.operation.name())
        Log.d(tag, request.operation.document())
        return chain.proceed(request).map { response ->
            Log.d(tag, request.operation.name())
            Log.d(tag, gson.toJson(response.data))
            response.errors?.let { Log.e(tag, gson.toJson(it)) }
            response
        }
    }
}
