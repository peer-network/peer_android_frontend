package eu.peernetwork.app.module.wallet

import dagger.Module

@Module(includes = [
    ApiModule::class,
    RepositoryModule::class,
    InteractorModule::class,
])
interface WalletModule
