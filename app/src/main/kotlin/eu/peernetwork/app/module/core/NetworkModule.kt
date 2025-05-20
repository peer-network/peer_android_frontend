package eu.peernetwork.app.module.core

import dagger.Module
import dagger.Provides
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.service.NetworkResource
import eu.peernetwork.core.remote.provider.NetworkProvider
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
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun providesNetworkResource(delegate: NetworkResource.Delegate): NetworkResource = delegate

    @Provides
    @Singleton
    fun providesNetworkProvider(delegate: NetworkResource): NetworkProvider = delegate
}
