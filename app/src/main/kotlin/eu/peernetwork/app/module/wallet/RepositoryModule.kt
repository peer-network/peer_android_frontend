package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.repository.TransactionRepositoryDelegate
import eu.peernetwork.wallet.data.repository.WalletRepositoryDelegate
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindWalletRepository(delegate: WalletRepositoryDelegate): WalletRepository

    @Binds
    fun provideTransferRepository(delegate: TransactionRepositoryDelegate): TransactionRepository
}
