package eu.peernetwork.media.ui.interactor

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.core.graphics.scale
import androidx.core.net.toUri
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.interactor.ExtractThumbnailsInteractor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.roundToInt

class ExtractThumbnailsInteractorDelegate @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
) : ExtractThumbnailsInteractor {

    override suspend fun extract(
        path: String,
        frameSlots: Int,
        thumbWidth: Int
    ): Pair<Long, List<Bitmap>> = withContext(dispatcher.io) {

        val retriever = MediaMetadataRetriever().apply {
            val uri = path.toUri()
            if (uri.scheme == "content") {
                setDataSource(context, uri)
            } else {
                setDataSource(path)
            }
        }
        val duration = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )!!.toLong()

        val stepUs = duration * 1_000 / max(frameSlots - 1, 1)

        val frames: List<Bitmap> = (0 until frameSlots).map { idx ->
            async(dispatcher.io) {
                val timeUs = idx * stepUs
                retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                    ?.let { raw ->
                        val h = computeHeight(raw.width, raw.height, thumbWidth)
                        raw.scale(thumbWidth, h)
                    }
            }
        }.awaitAll().filterNotNull()

        retriever.release()
        duration to frames
    }

    private fun computeHeight(w: Int, h: Int, thumbW: Int): Int =
        (thumbW.toFloat() * h / w).roundToInt()
}
