package eu.peernetwork.app.module.messaging

import dagger.Module

@Module(includes = [
    ApiModule::class,
    RepositoryModule::class
])
interface MessagingModule