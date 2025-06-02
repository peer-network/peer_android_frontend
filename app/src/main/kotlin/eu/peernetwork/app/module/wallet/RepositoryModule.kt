package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.repository.TransferRepositoryDelegate
import eu.peernetwork.wallet.domain.repository.TransferRepository

@Module
interface RepositoryModule {
    @Binds
    fun provideTransferRepository(delegate: TransferRepositoryDelegate): TransferRepository
}
