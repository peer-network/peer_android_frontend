package eu.peernetwork.wallet.domain.model

sealed interface Filter {
    data object None : Filter
    data class Attribute(
        val type: Transaction.Type,
        val start: Long,
        val end: Long,
    )
}
