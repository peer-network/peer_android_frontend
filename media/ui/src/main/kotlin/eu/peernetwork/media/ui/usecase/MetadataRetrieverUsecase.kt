package eu.peernetwork.media.ui.usecase

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.model.UiMetadata
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MetadataRetrieverUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
) : ParameterizedSuspendableUseCase<MetadataRetrieverUsecase.Parameter, UiMetadata?> {

    override suspend fun invoke(param: Parameter): UiMetadata? = withContext(dispatcher.io) {
        if (param.type == UiMimeType.Video) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(param.url)
                val bitmap = retriever.getFrameAtTime(param.frame, MediaMetadataRetriever.OPTION_CLOSEST)
                val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt() ?: 0
                val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt() ?: 0
                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
                UiMetadata(
                    width = width,
                    height = height,
                    bitmap = bitmap,
                    duration = duration
                )
            } finally {
                try {
                    retriever.release()
                } catch (error: Throwable) {
                    error.printStackTrace()
                }
            }
        } else {
            BitmapFactory.decodeFile(param.url)?.let {
                UiMetadata(
                    width = it.width,
                    height = it.height,
                    bitmap = it,
                    duration = 0
                )
            }
        }
    }

    data class Parameter(
        val url: String,
        val type: UiMimeType,
        val frame: Long = 0
    )
}
