package eu.peernetwork.wallet.ui.provider

import eu.peernetwork.wallet.domain.interactor.WalletInteractor

interface WalletProvider : RepositoryProvider {
    fun walletInteractor(): WalletInteractor
}
