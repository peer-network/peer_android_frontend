package eu.peernetwork.app.service

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.app.interceptor.LoggingInterceptor
import eu.peernetwork.core.remote.provider.NetworkProvider
import eu.peernetwork.user.remote.interceptor.JwtInterceptor
import javax.inject.Inject
import javax.inject.Named

interface NetworkResource : NetworkProvider {
    fun baseUrl(url: String)

    class Delegate @Inject constructor(
        @Named("baseUrl") private val baseUrl: String,
        private val logger: LoggingInterceptor,
        private val jwtInterceptor: JwtInterceptor,
    ) : NetworkResource {
        private var client = ApolloClient.Builder()
            .serverUrl("$baseUrl/graphql")
            .addInterceptor(logger)
            .addInterceptor(jwtInterceptor)
            .build()

        override fun baseUrl(url: String) {
            client = ApolloClient.Builder()
                .serverUrl("$url/graphql")
                .addInterceptor(logger)
                .addInterceptor(jwtInterceptor)
                .build()
        }

        override fun client(): ApolloClient = client
    }
}
