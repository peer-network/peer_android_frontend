package eu.peernetwork.app.module.social

import dagger.Module

@Module(includes = [
    ApiModule::class,
    RepositoryModule::class,
])
interface SocialModule
