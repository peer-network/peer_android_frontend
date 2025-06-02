package eu.peernetwork.media.ui.usecase

import android.media.MediaMetadataRetriever
import eu.peernetwork.media.core.model.UiMediaData
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import javax.inject.Inject

class MetaDataUsecaseDelegate @Inject constructor() : MetaDataUsecase {
    override fun invoke(param: String): UiMediaData? {
        val retriever = MediaMetadataRetriever()
        return try {
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
