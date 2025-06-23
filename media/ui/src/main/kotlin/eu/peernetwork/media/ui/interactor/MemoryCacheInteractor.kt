package eu.peernetwork.media.ui.interactor

import android.graphics.Bitmap
import android.util.LruCache
import eu.peernetwork.media.ui.interactor.BitmapInteractor.BitmapAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest

class MemoryCacheInteractor(maxSize: Int) : BitmapInteractor, BitmapAdapter {
    private val observer = MutableSharedFlow<Long>(replay = 1)

    private val cache = object : LruCache<String, Bitmap>(maxSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }

    init { observer.tryEmit(System.currentTimeMillis()) }

    override fun get(key: String): Bitmap? = cache.get(key)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<Map<String, Bitmap?>> = observer.mapLatest {
        cache.snapshot()
    }

    override fun put(key: String, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }

    override fun notifyDatasetChanged() {
        observer.tryEmit(System.currentTimeMillis())
    }
}
