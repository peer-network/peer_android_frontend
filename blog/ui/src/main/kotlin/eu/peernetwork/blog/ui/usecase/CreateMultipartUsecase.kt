package eu.peernetwork.blog.ui.usecase

import android.content.Context
import android.util.Log
import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import kotlinx.coroutines.withContext
import eu.peernetwork.core.common.provider.Dispatcher
import java.io.File
import javax.inject.Inject

class CreateMultipartUsecase @Inject constructor(
    private val multipartUsecase: MultipartUsecase,
    private val annotationUsecase: AnnotationUsecase,
    private val interactor: ContentInteractor,
    private val context: Context,
    private val dispatcher: Dispatcher
) : ParameterizedSuspendableUseCase<Draft, UiPost> {

    override suspend fun invoke(param: Draft): UiPost = withContext(dispatcher.io) {
        val uploadedFiles = when (val type = param.type) {
            is Draft.Type.Text -> type.files
            is Draft.Type.Image -> type.files.map { multipartUsecase(it.resolveToFile(context)) }
            is Draft.Type.Video -> type.files.map { multipartUsecase(it.resolveToFile(context)) }
            is Draft.Type.Audio -> type.files.map { multipartUsecase(it.resolveToFile(context)) }
        }
        val updatedType = when (val type = param.type) {
            is Draft.Type.Text -> type
            is Draft.Type.Image -> type.copy(files = uploadedFiles)
            is Draft.Type.Video -> type.copy(files = uploadedFiles)
            is Draft.Type.Audio -> type.copy(files = uploadedFiles)
        }
        val updatedDraft = param.copy(type = updatedType)
        val content = try {
            interactor.create(updatedDraft)
        } catch (t: Throwable) {
            Log.e("CreateMultipartUsecase", "interactor.create failed", t)
            throw t
        }
        if (content == null) {
            throw IllegalStateException("interactor.create returned null content")
        }
        try {
            content.mapToPhoto(context) { annotationUsecase(it) }
        } catch (t: Throwable) {
            Log.e("CreateMultipartUsecase", "mapToPhoto failed on content=$content", t)
            throw t
        }
    }

    private fun String.resolveToFile(context: Context): File {
        return if (startsWith("data:")) {
            val base64Data = substringAfter(",")
            val mimeType = substringAfter("data:").substringBefore(";")
            val extension = when (mimeType) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                "image/gif" -> ".gif"
                "image/webp" -> ".webp"
                "audio/mpeg" -> ".mp3"
                "audio/wav" -> ".wav"
                "audio/ogg" -> ".ogg"
                "video/mp4" -> ".mp4"
                else -> ".bin"
            }
            val file = File.createTempFile("upload_", extension, context.cacheDir)
            val decodedBytes = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
            file.outputStream().use { it.write(decodedBytes) }
            file
        } else {
            File(this)
        }
    }
}
