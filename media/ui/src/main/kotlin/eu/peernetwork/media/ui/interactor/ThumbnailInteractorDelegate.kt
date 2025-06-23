package eu.peernetwork.media.ui.interactor

import android.graphics.Bitmap
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.annotation.DiskCache
import eu.peernetwork.media.core.annotation.MemoryCache
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.usecase.BitmapMergeUsecase
import eu.peernetwork.media.ui.usecase.ThumbnailUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThumbnailInteractorDelegate @Inject constructor(
    private val usecase: ThumbnailUsecase,
    private val bitmapMergeUsecase: BitmapMergeUsecase,
    @MemoryCache private val memory: BitmapInteractor,
    @DiskCache private val disk: BitmapInteractor,
    private val observer: BitmapInteractor.BitmapAdapter,
    private val dispatcher: Dispatcher
) : ThumbnailInteractor {

    override fun observe(): Flow<Map<String, Bitmap?>> = observer.observe()

    override fun get(url: String): Bitmap? {
        memory.get(url)?.let { return it }
        val bitmap = disk.get(url) ?: return null
        memory.put(url, bitmap)
        return bitmap
    }

    override suspend fun get(
        url: String,
        type: UiMimeType,
        blur: Int?,
        dimen: Pair<Float, Float>
    ): Bitmap? = withContext(dispatcher.io) {
        usecase(ThumbnailUsecase.Parameter(url, type, dimen, blur))
    }

    override suspend fun save(
        url: String,
        type: UiMimeType,
        bitmap: Bitmap
    ): Bitmap = withContext(dispatcher.io) {
        disk.put(url, bitmap)
        memory.put(url, bitmap)
        observer.notifyDatasetChanged()
        bitmap
    }

    override suspend fun merge(
        url: String,
        type: UiMimeType,
        width: Int,
        aspectRatio: Float,
        background: Bitmap,
        foreground: Bitmap
    ): Bitmap = withContext(dispatcher.io) {
        val bitmap = bitmapMergeUsecase(
            BitmapMergeUsecase.Parameter(width, aspectRatio, background, foreground)
        )
        disk.put(url, bitmap)
        memory.put(url, bitmap)
        observer.notifyDatasetChanged()
        bitmap
    }

    override suspend fun load(
        url: String,
        type: UiMimeType,
        blur: Int?,
        dimen: Pair<Float, Float>,
    ): Bitmap? = withContext(dispatcher.io) {
        var bitmap = get(url)
        if (bitmap == null) {
            usecase(ThumbnailUsecase.Parameter(url, type, dimen, blur))?.apply {
                disk.put(url, this)
                memory.put(url, this)
                bitmap = this
            }
        }
        observer.notifyDatasetChanged()
        bitmap
    }

    override fun invalidate() {
        observer.notifyDatasetChanged()
    }
}
