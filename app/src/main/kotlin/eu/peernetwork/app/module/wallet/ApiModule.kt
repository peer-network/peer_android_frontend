package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.data.api.WalletApi
import eu.peernetwork.wallet.remote.api.TransferApiDelegate
import eu.peernetwork.wallet.remote.api.WalletApiDelegate

@Module
interface ApiModule {
    @Binds
    fun bindWalletApi(delegate: WalletApiDelegate): WalletApi

    @Binds
    fun bindTransferApi(delegate: TransferApiDelegate): TransferApi
}
