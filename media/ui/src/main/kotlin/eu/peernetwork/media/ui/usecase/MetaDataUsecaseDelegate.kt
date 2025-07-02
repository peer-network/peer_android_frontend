package eu.peernetwork.media.ui.usecase

import android.media.MediaMetadataRetriever
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.model.UiMediaData
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MetaDataUsecaseDelegate @Inject constructor(
    private val dispatcher: Dispatcher
) : MetaDataUsecase {
    override suspend fun invoke(param: String): UiMediaData? = withContext(dispatcher.io) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(param, mapOf())
            val thumbnail = retriever.frameAtTime
            val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
            val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
            UiMediaData(width, height, thumbnail)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }
}
