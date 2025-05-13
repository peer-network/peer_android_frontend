package eu.peernetwork.app.module.core

import dagger.Binds
import dagger.Module
import eu.peernetwork.app.service.ResourceLoader
import eu.peernetwork.app.service.ResourceServiceDelegate
import eu.peernetwork.core.common.service.ResourceService

@Module
interface ServiceModule {
    @Binds
    fun resourceLoader(delegate: ResourceServiceDelegate): ResourceLoader

    @Binds
    fun resourceService(delegate: ResourceServiceDelegate): ResourceService
}
