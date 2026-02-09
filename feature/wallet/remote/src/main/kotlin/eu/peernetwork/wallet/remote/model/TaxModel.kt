package eu.peernetwork.wallet.remote.model

import com.google.gson.annotations.SerializedName

data class TaxModel(
    val data: Token
) {
    data class Token(
        @SerializedName("TOKENOMICS") val item: Item
    )

    data class Item(@SerializedName("FEES") val value: Price)

    data class Price(
        @SerializedName("INVITATION") val invitation: Double,
        @SerializedName("POOL") val pool: Double,
        @SerializedName("PEER") val peer: Double,
        @SerializedName("BURN") val burn: Double
    )
}
