package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.repository.WalletRepositoryDelegate
import eu.peernetwork.wallet.domain.repository.WalletRepository

@Module(includes = [
    ApiModule::class,
    RepositoryModule::class
])
interface WalletModule {
    @Binds
    fun bindWalletRepository(delegate: WalletRepositoryDelegate): WalletRepository
}
