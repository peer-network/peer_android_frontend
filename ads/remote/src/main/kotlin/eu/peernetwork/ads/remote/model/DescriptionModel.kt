package eu.peernetwork.ads.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class DescriptionModel(
    val data: Configuration
) {
    data class Configuration(@SerializedName("TOKENOMICS") val ads: Properties)

    data class Properties(@SerializedName("ACTION_TOKEN_PRICES") val price: Price)

    data class Price(
        @SerializedName("advertisementBasic") val basic: BigDecimal,
        @SerializedName("advertisementPinned") val pinned: BigDecimal
    )
}
