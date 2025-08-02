package eu.peernetwork.media.ui.model

import android.graphics.Bitmap

data class UiMetadata(
    val width: Int,
    val height: Int,
    val duration: Long,
    val bitmap: Bitmap?,
)
