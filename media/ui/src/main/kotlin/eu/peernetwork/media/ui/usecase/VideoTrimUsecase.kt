package eu.peernetwork.media.ui.usecase

import ai.instavision.ffmpegkit.FFmpegKit
import ai.instavision.ffmpegkit.ReturnCode
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.net.toFile
import androidx.media3.common.util.UnstableApi
import eu.peernetwork.core.common.exception.BusinessException
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiOffset
import eu.peernetwork.media.ui.extension.toMd5
import kotlinx.coroutines.CancellableContinuation
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
        val command = arrayOf(
            "-ss", (param.offset.start / 1000).toString(),
            "-i", source.path,
            "-to", (param.offset.stop / 1000).toString(),
            "-avoid_negative_ts", "make_zero",
            "-y",
            outputFile.path
        )
        val session = FFmpegKit.executeWithArguments(command)
        if (ReturnCode.isSuccess(session.returnCode)) {
            resumeWith(Result.success(outputFile))
        } else {
            outputFile.deleteOnExit()
            resumeWithException(BusinessException("${session.failStackTrace}", source.path))
        }
        invokeOnCancellation {
            outputFile.deleteOnExit()
        }
    }

    data class Parameter(
        val uri: Uri,
        val offset: UiOffset
    )
}
