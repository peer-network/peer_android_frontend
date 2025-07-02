package eu.peernetwork.media.core.interactor

import android.graphics.Bitmap
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.flow.Flow

interface ThumbnailInteractor {
    fun observe(): Flow<Map<String, Bitmap?>>

    suspend fun get(url: String): Bitmap?

    suspend fun get(
        url: String,
        type: UiMimeType,
        dimen: Pair<Float, Float>,
    ): Bitmap?

    suspend fun save(
        url: String,
        bitmap: Bitmap
    ): Bitmap

    suspend fun merge(
        url: String,
        width: Int,
        aspectRatio: Float,
        background: Bitmap,
        foreground: Bitmap,
    ): Bitmap

    suspend fun load(
        url: String,
        type: UiMimeType,
        dimen: Pair<Float, Float> = Pair(350f, 350f),
    ): Bitmap?

    suspend fun blur(bitmap: Bitmap, blur: Int): Bitmap

    fun invalidate()
}
