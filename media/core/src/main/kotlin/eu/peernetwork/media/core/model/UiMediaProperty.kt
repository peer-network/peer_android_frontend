package eu.peernetwork.media.core.model

import android.graphics.Bitmap

data class UiMediaProperty(
    val width: Int,
    val height: Int,
    val duration: Long,
    val bitmap: Bitmap?,
)
