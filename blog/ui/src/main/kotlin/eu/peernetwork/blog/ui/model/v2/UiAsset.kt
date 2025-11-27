package eu.peernetwork.blog.ui.model.v2

import kotlinx.collections.immutable.ImmutableList

data class UiAsset(
    val ratio: Float,
    val media: ImmutableList<UiMedia>,
)
