package eu.peernetwork.media.ui.usecase

import android.content.Context
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.*
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedFlowUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.File
import javax.inject.Inject

class VideoTrimUsecase @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
) : ParameterizedFlowUseCase<
        VideoTrimUsecase.Parameter,
        VideoTrimUsecase.Result> {

    data class Parameter(
        val input: String,
        val cacheDir: File,
        val startMs: Long,
        val endMs: Long
    )

    sealed class Result {
        data class Progress(val pct: Int) : Result()
        data class Success(val file: File) : Result()
        data class Failure(val cause: Throwable) : Result()
    }

    @OptIn(UnstableApi::class)
    override fun invoke(param: Parameter): Flow<Result> = callbackFlow {

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
            .addListener(object : Transformer.Listener {
                override fun onCompleted(
                    composition: Composition,
                    exportResult: ExportResult
                ) {
                    trySend(Result.Progress(100))
                    trySend(Result.Success(out))
                    close()
                }

                override fun onError(
                    composition: Composition,
                    exportResult: ExportResult,
                    exportException: ExportException
                ) {
                    trySend(Result.Failure(exportException))
                    close()
                }
            })
            .build()

        withContext(dispatcher.main) {
            transformer.start(edited, out.absolutePath)
        }

        launch(dispatcher.io) {
            val holder = ProgressHolder()
            while (isActive) {
                withContext(dispatcher.main) {
                    if (transformer.getProgress(holder)
                        == Transformer.PROGRESS_STATE_AVAILABLE
                    ) {
                        trySend(Result.Progress(holder.progress))
                    }
                }
            }
        }
        awaitClose {
            launch(dispatcher.main) { transformer.cancel() }
        }
    }
}
