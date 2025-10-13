package eu.peernetwork.app.module.core

import dagger.Module
import dagger.Provides
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interceptor.JwtOkHttpInterceptor
import eu.peernetwork.app.service.NetworkService
import eu.peernetwork.core.remote.api.RequestClient
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
internal object NetworkModule {
    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Named("mediaUrl")
    fun provideMediaUrl(): String = BuildConfig.MEDIA_URL

    @Provides
    @Named("inviteUrl")
    fun provideInviteUrl(): String = BuildConfig.INVITE_URL

    @Provides
    fun provideOkHttpClient(
        jwtOkHttpInterceptor: JwtOkHttpInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(jwtOkHttpInterceptor)
        .build()

    @Provides
    @Singleton
    fun providesNetworkResource(delegate: NetworkService.Delegate): NetworkService = delegate

    @Provides
    @Singleton
    fun providesNetworkProvider(delegate: NetworkService): RequestClient = delegate
}
