package eu.peernetwork.app.module.core

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.user.remote.interceptor.JwtInterceptor

@Module
internal object NetworkModule {
    @Provides
    fun provideApolloClient(jwtInterceptor: JwtInterceptor): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BASE_URL)
            .addInterceptor(jwtInterceptor)
            .build()
    }
}
