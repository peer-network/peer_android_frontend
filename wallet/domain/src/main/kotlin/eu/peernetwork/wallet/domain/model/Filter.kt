package eu.peernetwork.wallet.domain.model

sealed class Filter(
    val type: Transaction.Type?,
    val start: String?,
    val end: String?,
) {
    data object None : Filter(null, null, null)
}
