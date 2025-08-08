package eu.peernetwork.media.ui.interactor

import android.graphics.Bitmap
import androidx.core.graphics.scale
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.annotation.DiskCache
import eu.peernetwork.media.core.annotation.MemoryCache
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMediaProperty
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.usecase.BitmapMergeUsecase
import eu.peernetwork.media.ui.usecase.BlurUsecase
import eu.peernetwork.media.ui.usecase.MetadataRetrieverUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

class ThumbnailInteractorDelegate @Inject constructor(
    private val blurUsecase: BlurUsecase,
    private val bitmapMergeUsecase: BitmapMergeUsecase,
    private val metadataRetrieverUsecase: MetadataRetrieverUsecase,
    @DiskCache private val disk: BitmapInteractor,
    @MemoryCache private val memory: BitmapInteractor,
    private val observer: BitmapInteractor.BitmapAdapter,
    private val dispatcher: Dispatcher
) : ThumbnailInteractor {

    override fun observe(): Flow<Map<String, Bitmap?>> = observer
        .observe()
        .flowOn(dispatcher.io)

    override suspend fun get(url: String): Bitmap? = withContext(dispatcher.io) {
        val cache = memory.get(url)
        if (cache != null) {
            cache
        } else {
            val bitmap = disk.get(url)
            if (bitmap != null) {
                memory.put(url, bitmap)
            }
            bitmap
        }
    }

    override suspend fun get(
        url: String,
        type: UiMimeType
    ): Bitmap? = withContext(dispatcher.io) {
        try {
            metadataRetrieverUsecase(
                MetadataRetrieverUsecase.Parameter(
                    url = url,
                    type = type
                )
            )?.bitmap
        } catch (error: Throwable) {
            error.printStackTrace()
            null
        }
    }

    override suspend fun get(
        url: String,
        type: UiMimeType,
        dimen: Pair<Float, Float>
    ): Bitmap? = withContext(dispatcher.io) {
        get(url, type)?.let{ scale(it, dimen) }
    }

    override suspend fun get(url: String, type: UiMimeType, frame: Long): UiMediaProperty? {
        return metadataRetrieverUsecase(
            MetadataRetrieverUsecase.Parameter(
                url = url,
                type = type,
                frame = frame
            )
        )
    }

    override suspend fun save(url: String, bitmap: Bitmap): Bitmap = withContext(dispatcher.io) {
        disk.put(url, bitmap)
        memory.put(url, bitmap)
        bitmap
    }

    override suspend fun scale(bitmap: Bitmap, dimen: Pair<Float, Float>): Bitmap = withContext(dispatcher.io) {
        val scale = min(dimen.first / bitmap.width, dimen.second / bitmap.height)
        val scaledWidth = (bitmap.width * scale).toInt()
        val scaledHeight = (bitmap.height * scale).toInt()
        bitmap.scale(scaledWidth, scaledHeight)
    }

    override suspend fun merge(
        url: String,
        aspectRatio: Float,
        background: Bitmap,
        foreground: Bitmap,
        width: Int,
        height: Int,
        fit: Boolean
    ): Bitmap = withContext(dispatcher.io) {
        val bitmap = bitmapMergeUsecase(BitmapMergeUsecase.Parameter(
            aspectRatio,
            background,
            foreground,
            width,
            height,
            fit
        ))
        save(url, bitmap)
    }

    override suspend fun load(
        url: String,
        type: UiMimeType,
        dimen: Pair<Float, Float>,
    ): Bitmap? = withContext(dispatcher.io) {
        var bitmap = get(url)
        if (bitmap == null) {
            get(url, type, dimen)?.apply {
                disk.put(url, this)
                memory.put(url, this)
                bitmap = this
            }
        }
        bitmap
    }

    override suspend fun blur(bitmap: Bitmap, blur: Int): Bitmap {
        return blurUsecase(BlurUsecase.Parameter(bitmap.scale(50, 50), blur))
    }

    override fun invalidate() {
        observer.notifyDatasetChanged()
    }
}
