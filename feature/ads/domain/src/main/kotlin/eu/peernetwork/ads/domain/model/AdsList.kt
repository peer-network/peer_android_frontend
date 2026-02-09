package eu.peernetwork.ads.domain.model

data class AdsList(
    val count: Int,
    val offset: Int,
    val metrics: Metrics,
    val items: List<Ads>
)
