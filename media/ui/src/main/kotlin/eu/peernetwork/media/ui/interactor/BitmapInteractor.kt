package eu.peernetwork.media.ui.interactor

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface BitmapInteractor {
    fun get(key: String): Bitmap?

    fun put(key: String, bitmap: Bitmap)

    interface BitmapAdapter {
        fun observe(): Flow<Map<String, Bitmap?>>

        fun notifyDatasetChanged()
    }
}
