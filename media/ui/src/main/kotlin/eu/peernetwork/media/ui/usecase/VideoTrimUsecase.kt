package eu.peernetwork.media.ui.usecase

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.net.toFile
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiOffset
import eu.peernetwork.media.ui.extension.toMd5
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

@OptIn(UnstableApi::class)
class VideoTrimUsecase @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
): ParameterizedSuspendableUseCase<VideoTrimUsecase.Parameter, File> {
    override suspend fun invoke(param: Parameter): File = withContext(dispatcher.io) {
        if (param.offset is UiOffset.None) {
            param.uri.toFile()
        } else {
            suspendCancellableCoroutine { continuation ->
                continuation.handleTrimming(param)
            }
        }
    }

    private fun CancellableContinuation<File>.handleTrimming(param: Parameter) {
        val source = param.uri.toFile()
        val path = "${this@VideoTrimUsecase.context.cacheDir}/${source.path.toMd5()}_${source.name}"
        val outputFile = File(path)
        if (outputFile.exists()) outputFile.delete()
        val mediaItem = MediaItem.Builder()
            .setUri(param.uri)
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(param.offset.start)
                    .setEndPositionMs(param.offset.stop)
                    .build()
            ).build()
        val transformer = Transformer.Builder(this@VideoTrimUsecase.context)
            .setMaxDelayBetweenMuxerSamplesMs(5000)
            .addListener(object : Transformer.Listener {
                override fun onCompleted(
                    composition: Composition,
                    exportResult: ExportResult
                ) {
                    if (isActive) {
                        resumeWith(Result.success(outputFile))
                    }
                }
                override fun onError(
                    composition: Composition,
                    exportResult: ExportResult,
                    exportException: ExportException
                ) {
                    outputFile.deleteOnExit()
                    if (isActive) {
                        resumeWithException(exportException)
                    }
                }
            }).build()
        val job = CoroutineScope(Dispatchers.Main).launch {
            try {
                transformer.start(mediaItem, path)
            } catch (e: Exception) {
                outputFile.deleteOnExit()
                if (isActive) {
                    resumeWithException(e)
                }
            }
        }
        invokeOnCancellation {
            job.cancel()
            outputFile.deleteOnExit()
        }
    }

    data class Parameter(
        val uri: Uri,
        val offset: UiOffset
    )
}
