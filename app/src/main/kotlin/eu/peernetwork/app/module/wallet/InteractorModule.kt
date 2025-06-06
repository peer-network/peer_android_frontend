package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.interactor.WalletInteractorDelegate
import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import javax.inject.Singleton

@Module
interface InteractorModule {
    @Binds
    @Singleton
    fun walletInteractor(delegate: WalletInteractorDelegate): WalletInteractor
}
