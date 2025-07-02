package eu.peernetwork.media.ui.interactor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.core.graphics.scale
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.annotation.DiskCache
import eu.peernetwork.media.core.annotation.MemoryCache
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.usecase.BitmapMergeUsecase
import eu.peernetwork.media.ui.usecase.BlurUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

class ThumbnailInteractorDelegate @Inject constructor(
    private val blurUsecase: BlurUsecase,
    private val bitmapMergeUsecase: BitmapMergeUsecase,
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
        type: UiMimeType,
        dimen: Pair<Float, Float>
    ): Bitmap? = withContext(dispatcher.io) {
        if (type == UiMimeType.Video) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(url)
                retriever.getFrameAtTime(100_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } catch (error: Throwable) {
                error.printStackTrace()
                null
            } finally {
                try {
                    retriever.release()
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
        } else {
            BitmapFactory.decodeFile(url)
        }?.let{
            val scale = min(dimen.first / it.width, dimen.second / it.height)
            val scaledWidth = (it.width * scale).toInt()
            val scaledHeight = (it.height * scale).toInt()
            it.scale(scaledWidth, scaledHeight)
        }
    }

    override suspend fun save(url: String, bitmap: Bitmap): Bitmap = withContext(dispatcher.io) {
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
        val bitmap = bitmapMergeUsecase(BitmapMergeUsecase.Parameter(
            width,
            aspectRatio,
            background,
            foreground
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
        observer.notifyDatasetChanged()
        bitmap
    }

    override suspend fun blur(bitmap: Bitmap, blur: Int): Bitmap {
        return blurUsecase(BlurUsecase.Parameter(bitmap.scale(50, 50), blur))
    }

    override fun invalidate() {
        observer.notifyDatasetChanged()
    }
}
