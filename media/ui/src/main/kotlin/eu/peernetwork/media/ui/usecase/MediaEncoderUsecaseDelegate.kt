package eu.peernetwork.media.ui.usecase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import eu.peernetwork.core.common.usecase.FileEncoderUsecase
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import javax.inject.Inject

class MediaEncoderUsecaseDelegate @Inject constructor(
    private val context: Context,
    private val fileEncoderUsecase: FileEncoderUsecase
) : MediaEncoderUsecase {
    override fun invoke(param: Uri): String? {
        return context.contentResolver.openInputStream(param)?.let {
            if (param.scheme == ContentResolver.SCHEME_CONTENT) {
                context.contentResolver.getType(param)
            } else {
                val extension = MimeTypeMap.getFileExtensionFromUrl(param.toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
            }?.let { type ->
                fileEncoderUsecase(FileEncoderUsecase.Parameter(type, it))
            }
        }
    }
}
