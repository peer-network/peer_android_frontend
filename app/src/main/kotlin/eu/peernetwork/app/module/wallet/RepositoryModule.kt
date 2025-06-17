package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.repository.RewardRepositoryDelegate
import eu.peernetwork.wallet.data.repository.TaxRepositoryDelegate
import eu.peernetwork.wallet.data.repository.TransactionRepositoryDelegate
import eu.peernetwork.wallet.data.repository.WalletRepositoryDelegate
import eu.peernetwork.wallet.domain.repository.RewardRepository
import eu.peernetwork.wallet.domain.repository.TaxRepository
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    fun bindTaxRepository(delegate: TaxRepositoryDelegate): TaxRepository

    @Binds
    fun bindWalletRepository(delegate: WalletRepositoryDelegate): WalletRepository

    @Binds
    fun provideTransferRepository(delegate: TransactionRepositoryDelegate): TransactionRepository

    @Binds
    @Singleton
    fun provideRewardRepository(delegate: RewardRepositoryDelegate): RewardRepository
}
