package eu.peernetwork.media.core.interactor

import android.graphics.Bitmap
import eu.peernetwork.media.core.model.UiMediaProperty
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.flow.Flow

interface ThumbnailInteractor {
    fun observe(): Flow<Map<String, Bitmap?>>

    suspend fun get(url: String): Bitmap?

    suspend fun get(url: String, type: UiMimeType): Bitmap?

    suspend fun get(
        url: String,
        type: UiMimeType,
        dimen: Pair<Float, Float>,
    ): Bitmap?

    suspend fun get(url: String, type: UiMimeType, frame: Long): UiMediaProperty?

    suspend fun save(
        url: String,
        bitmap: Bitmap
    ): Bitmap

    suspend fun scale(bitmap: Bitmap, dimen: Pair<Float, Float>): Bitmap

    suspend fun merge(
        url: String,
        aspectRatio: Float,
        background: Bitmap,
        foreground: Bitmap,
        width: Int,
        height: Int = (width / aspectRatio).toInt(),
        fit: Boolean = false
    ): Bitmap

    suspend fun load(
        url: String,
        type: UiMimeType,
        dimen: Pair<Float, Float> = Pair(350f, 350f),
    ): Bitmap?

    suspend fun blur(bitmap: Bitmap, blur: Int): Bitmap

    fun invalidate()
}
