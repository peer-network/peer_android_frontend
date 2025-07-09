package eu.peernetwork.media.ui.usecase

import android.content.Context
import android.net.Uri
import eu.peernetwork.media.core.model.UiMimeType
import javax.inject.Inject

class CroppedPreviewUsecase @Inject constructor(
    private val context: Context,
    private val decodeAndCacheThumbnailUsecase: DecodeAndCacheThumbnailUsecase
) {

    suspend operator fun invoke(
        uri: Uri,
        key: String,
        px: Int,
        mime: UiMimeType = UiMimeType.Photo
    ): String = context.contentResolver.openInputStream(uri)!!.use { stream ->
        decodeAndCacheThumbnailUsecase(
            DecodeAndCacheThumbnailUsecase.Params(key, stream, px, mime)
        )
        key
    }
}
