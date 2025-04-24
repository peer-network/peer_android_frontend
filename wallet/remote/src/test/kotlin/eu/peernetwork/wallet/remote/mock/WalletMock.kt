package eu.peernetwork.wallet.remote.mock

import wallet.wallet.eu.peernetwork.wallet.remote.CurrentliquidityQuery

object WalletMock {
    fun wallet(): CurrentliquidityQuery.Currentliquidity {
        return CurrentliquidityQuery.Currentliquidity(100L)
    }
}
