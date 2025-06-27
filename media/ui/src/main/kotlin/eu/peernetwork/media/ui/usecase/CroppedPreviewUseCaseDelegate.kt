package eu.peernetwork.media.ui.usecase

import android.content.Context
import android.net.Uri
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.usecase.CroppedPreviewUseCase
import eu.peernetwork.media.core.usecase.DecodeAndCacheThumbnailUsecase
import javax.inject.Inject

class CroppedPreviewUsecaseDelegate @Inject constructor(
    private val context: Context,
    private val decodeAndCacheThumbnailUsecase: DecodeAndCacheThumbnailUsecase
) : CroppedPreviewUseCase {

    override suspend fun invoke(uri: Uri, key: String, px: Int, mime: UiMimeType): String =
        context.contentResolver.openInputStream(uri)!!.use { stream ->
            decodeAndCacheThumbnailUsecase(
                DecodeAndCacheThumbnailUsecase.Params(
                    key, stream, px, mime
                )
            )
            key
        }
}
