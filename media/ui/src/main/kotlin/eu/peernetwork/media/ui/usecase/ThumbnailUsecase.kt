package eu.peernetwork.media.ui.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiMimeType
import javax.inject.Inject
import androidx.core.graphics.scale
import com.commit451.nativestackblur.NativeStackBlur
import kotlin.math.min

class ThumbnailUsecase @Inject constructor() : ParameterizedSuspendableUseCase<ThumbnailUsecase.Parameter, Bitmap?> {
    override suspend fun invoke(param: Parameter): Bitmap? {
        return if (param.type == UiMimeType.Video) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(param.url)
                retriever.getFrameAtTime(100_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } catch (error: Throwable) {
                error.printStackTrace()
                null
            } finally {
                retriever.release()
            }
        } else {
            BitmapFactory.decodeFile(param.url)
        }?.let{
            val scale = min(param.dimen.first / it.width, param.dimen.second / it.height)
            val scaledWidth = (it.width * scale).toInt()
            val scaledHeight = (it.height * scale).toInt()
            (param.blur?.let { blur ->
                NativeStackBlur.process(it, blur)
            } ?: it).scale(scaledWidth, scaledHeight)
        }
    }

    data class Parameter(
        val url: String,
        val type: UiMimeType,
        val dimen: Pair<Float, Float>,
        val blur: Int?,
    )
}
