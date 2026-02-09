package eu.peernetwork.blog.ui.model

import kotlinx.collections.immutable.ImmutableList

data class UiAsset(
    val ratio: Float,
    val hasCover: Boolean,
    val media: ImmutableList<UiMedia>,
)
