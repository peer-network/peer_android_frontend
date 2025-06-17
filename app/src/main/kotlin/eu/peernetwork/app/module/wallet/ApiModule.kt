package eu.peernetwork.app.module.wallet

import dagger.Binds
import dagger.Module
import eu.peernetwork.wallet.data.api.ReferralApi
import eu.peernetwork.wallet.data.api.RewardApi
import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.data.api.WalletApi
import eu.peernetwork.wallet.remote.api.ReferralApiDelegate
import eu.peernetwork.wallet.remote.api.RewardApiDelegate
import eu.peernetwork.wallet.remote.api.TransferApiDelegate
import eu.peernetwork.wallet.remote.api.WalletApiDelegate

@Module
interface ApiModule {
    @Binds
    fun bindWalletApi(delegate: WalletApiDelegate): WalletApi

    @Binds
    fun bindTransferApi(delegate: TransferApiDelegate): TransferApi

    @Binds
    fun bindReferralApi(delegate: ReferralApiDelegate): ReferralApi

    @Binds
    fun bindRewardApi(delegate: RewardApiDelegate): RewardApi
}
