package eu.peernetwork.wallet.remote.mock

import wallet.wallet.eu.peernetwork.wallet.remote.CurrentliquidityQuery

object WalletMock {
    fun wallet(): CurrentliquidityQuery.Balance {
        return CurrentliquidityQuery.Balance(100L)
    }
}
