package eu.peernetwork.app.service

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.app.interceptor.LoggingInterceptor
import eu.peernetwork.app.interceptor.NetworkErrorInterceptor
import eu.peernetwork.app.interceptor.ResourceInterceptor
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.user.remote.interceptor.JwtInterceptor
import javax.inject.Inject

interface NetworkService : RequestClient {
    class Delegate @Inject constructor(
        private val provider: ResourceInteractor,
        private val network: NetworkErrorInterceptor,
        private val logger: LoggingInterceptor,
        private val jwtInterceptor: JwtInterceptor,
        private val resourceInterceptor: ResourceInterceptor
    ) : NetworkService {
        private var cache: String = provider.getBaseUrl()

        private var client = ApolloClient.Builder()
            .serverUrl("$cache/graphql")
            .addInterceptor(logger)
            .addInterceptor(resourceInterceptor)
            .addInterceptor(jwtInterceptor)
            .addInterceptor(network)
            .build()

        override fun invoke(): ApolloClient {
            if (provider.getBaseUrl() == cache) {
                cache = provider.getBaseUrl()
                client = ApolloClient.Builder()
                    .serverUrl("$cache/graphql")
                    .addInterceptor(resourceInterceptor)
                    .addInterceptor(logger)
                    .addInterceptor(jwtInterceptor)
                    .addInterceptor(network)
                    .build()
            }
            return client
        }
    }
}