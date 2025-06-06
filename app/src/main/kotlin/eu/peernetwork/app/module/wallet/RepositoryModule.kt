package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.repository.TransferRepositoryDelegate
import eu.peernetwork.wallet.data.repository.WalletRepositoryDelegate
import eu.peernetwork.wallet.domain.repository.TransferRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindWalletRepository(delegate: WalletRepositoryDelegate): WalletRepository

    @Binds
    fun provideTransferRepository(delegate: TransferRepositoryDelegate): TransferRepository
}
