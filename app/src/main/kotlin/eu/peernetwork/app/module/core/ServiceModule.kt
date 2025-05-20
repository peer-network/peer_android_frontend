package eu.peernetwork.app.module.core

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import eu.peernetwork.app.service.ResourceLoader
import eu.peernetwork.app.service.ResourceServiceDelegate
import eu.peernetwork.core.common.service.ResourceService

@Module
object ServiceModule {
    @Provides
    fun resourceLoader(delegate: ResourceServiceDelegate): ResourceLoader = delegate

    @Provides
    fun resourceService(delegate: ResourceServiceDelegate): ResourceService = delegate

    @Provides
    fun provideRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
}
