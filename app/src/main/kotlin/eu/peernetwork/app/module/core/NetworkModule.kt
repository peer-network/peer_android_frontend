package eu.peernetwork.app.module.core

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interceptor.LoggingInterceptor
import eu.peernetwork.user.remote.interceptor.AuthInterceptor
import eu.peernetwork.user.remote.interceptor.JwtInterceptor
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
internal object NetworkModule {
    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Named("mediaUrl")
    fun provideMediaUrl(): String = BuildConfig.MEDIA_URL

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun provideApolloClient(
        @Named("baseUrl") baseUrl: String,
        logger: LoggingInterceptor,
        jwtInterceptor: JwtInterceptor,
        authInterceptor: AuthInterceptor,
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("$baseUrl/graphql")
            .addInterceptor(logger)
            .addInterceptor(jwtInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }
}
