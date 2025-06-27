package eu.peernetwork.media.core.interactor

import android.graphics.Bitmap

interface ExtractThumbnailsInteractor {
    suspend fun extract(
        path: String,
        frameSlots: Int,
        thumbWidth: Int
    ): Pair<Long, List<Bitmap>>
}
