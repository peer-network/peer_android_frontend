package eu.peernetwork.media.ui.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.graphics.scale
import kotlin.math.min

class ThumbnailUsecase @Inject constructor(
    private val dispatcher: Dispatcher
) : ParameterizedSuspendableUseCase<ThumbnailUsecase.Parameter, Bitmap?> {
    override suspend fun invoke(param: Parameter): Bitmap? = withContext(dispatcher.io) {
        if (param.type == UiMimeType.Video) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(param.thumbnail)
                retriever.getFrameAtTime(100_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } catch (error: Throwable) {
                error.printStackTrace()
                null
            } finally {
                retriever.release()
            }
        } else {
            BitmapFactory.decodeFile(param.thumbnail)
        }?.let{
            val scale = min(200f / it.width, 200f / it.height)
            val scaledWidth = (it.width * scale).toInt()
            val scaledHeight = (it.height * scale).toInt()
            it.scale(scaledWidth, scaledHeight)
        }
    }

    data class Parameter(val thumbnail: String, val type: UiMimeType)
}
