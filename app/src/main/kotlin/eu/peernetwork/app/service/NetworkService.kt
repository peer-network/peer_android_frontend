package eu.peernetwork.app.service

import com.apollographql.apollo3.ApolloClient
import eu.peernetwork.app.interceptor.LoggingInterceptor
import eu.peernetwork.core.common.interactor.UrlInteractor
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.user.remote.interceptor.JwtInterceptor
import javax.inject.Inject

interface NetworkService : RequestClient {
    class Delegate @Inject constructor(
        private val provider: UrlInteractor,
        private val logger: LoggingInterceptor,
        private val jwtInterceptor: JwtInterceptor,
    ) : NetworkService {
        private var cache: String = provider.get()

        private var client = ApolloClient.Builder()
            .serverUrl("$cache/graphql")
            .addInterceptor(logger)
            .addInterceptor(jwtInterceptor)
            .build()

        override fun invoke(): ApolloClient {
            if (provider.get() == cache) {
                cache = provider.get()
                client = ApolloClient.Builder()
                    .serverUrl("$cache/graphql")
                    .addInterceptor(logger)
                    .addInterceptor(jwtInterceptor)
                    .build()
            }
            return client
        }
    }
}