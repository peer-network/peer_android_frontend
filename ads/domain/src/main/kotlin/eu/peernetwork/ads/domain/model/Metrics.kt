package eu.peernetwork.ads.domain.model

data class Metrics(
    val token: Float,
    val euro: Float,
    val likes: Int,
    val dislikes: Int,
    val views: Int,
    val comments: Int,
    val report: Int
)
