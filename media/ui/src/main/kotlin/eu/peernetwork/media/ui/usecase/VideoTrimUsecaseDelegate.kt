package eu.peernetwork.media.ui.usecase

import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.usecase.VideoTrimUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

import android.content.Context
import android.os.Looper
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ProgressHolder
import androidx.media3.transformer.Transformer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class VideoTrimUsecaseDelegate @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
) : VideoTrimUsecase {

    @OptIn(UnstableApi::class)
    override fun invoke(param: VideoTrimUsecase.Parameter)
            : Flow<VideoTrimUsecase.Result> = callbackFlow {

        val out = File(param.cacheDir, "trim_${System.currentTimeMillis()}.mp4")

        val mediaItem = MediaItem.Builder()
            .setUri(param.input.toUri())
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(param.startMs)
                    .setEndPositionMs(param.endMs)
                    .build()
            ).build()

        val edited = EditedMediaItem.Builder(mediaItem).build()

        val transformer = Transformer.Builder(context)
            .setLooper(Looper.getMainLooper())
            .addListener(object : Transformer.Listener {
                override fun onCompleted(
                    composition: androidx.media3.transformer.Composition,
                    exportResult: androidx.media3.transformer.ExportResult
                ) {
                    trySend(VideoTrimUsecase.Result.Progress(100))
                    trySend(VideoTrimUsecase.Result.Success(out))
                    close()
                }

                override fun onError(
                    composition: androidx.media3.transformer.Composition,
                    exportResult: androidx.media3.transformer.ExportResult,
                    exportException: androidx.media3.transformer.ExportException
                ) {
                    trySend(VideoTrimUsecase.Result.Failure(exportException))
                    close()
                }
            })
            .build()

        withContext(dispatcher.main) {
            transformer.start(edited, out.absolutePath)
        }

        launch(dispatcher.main) {
            val holder = ProgressHolder()
            while (isActive) {
                if (transformer.getProgress(holder)
                    == Transformer.PROGRESS_STATE_AVAILABLE
                ) {
                    trySend(VideoTrimUsecase.Result.Progress(holder.progress))
                }
                delay(100)
            }
        }

        awaitClose {
            launch(dispatcher.main) { transformer.cancel() }
        }
    }.flowOn(dispatcher.io)
}


