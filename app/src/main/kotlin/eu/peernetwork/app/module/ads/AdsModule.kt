package eu.peernetwork.app.module.ads

import dagger.Module

@Module(includes = [
    ApiModule::class,
    RendererModule::class,
    RepositoryModule::class
])
interface AdsModule
