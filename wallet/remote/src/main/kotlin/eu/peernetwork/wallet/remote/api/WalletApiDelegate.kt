package eu.peernetwork.wallet.remote.api

import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.wallet.data.api.WalletApi
import eu.peernetwork.wallet.domain.exception.WalletException
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.remote.mapper.toBigDecimalOrNull
import wallet.wallet.eu.peernetwork.wallet.remote.CurrentliquidityQuery
import javax.inject.Inject

class WalletApiDelegate @Inject constructor(
    private val client: RequestClient,
) : WalletApi {
    override suspend fun get(): Wallet {
        val query = CurrentliquidityQuery()
        val response = client().query(query).executeOrThrow()
        val data = response.getOrThrow().balance
        return data.currentliquidity?.let {
            it.toBigDecimalOrNull()?.let { Wallet(
                it,
                rate = .10f,
                currency = "€"
            ) }
        } ?: throw WalletException()
    }
}
