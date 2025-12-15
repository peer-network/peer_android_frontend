package eu.peernetwork.ads.domain.model

import java.math.BigDecimal

data class Ads(
    val id: String,
    val from: Long,
    val to: Long,
    val status: Boolean,
    val cost: BigDecimal,
    val earning: BigDecimal,
    val content: Content,
    val metrics: Metrics,
) {
    sealed interface Plan {
        data class Basic(val price: BigDecimal): Plan
        data class Pinned(val price: BigDecimal): Plan
    }
}
