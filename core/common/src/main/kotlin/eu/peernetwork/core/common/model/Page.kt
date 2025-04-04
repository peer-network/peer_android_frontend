package eu.peernetwork.core.common.model

data class Page<T>(
    val count: Int,
    val offset: Int,
    val items: List<T>
)
