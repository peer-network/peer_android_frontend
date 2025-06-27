package eu.peernetwork.media.core.usecase

import android.net.Uri
import eu.peernetwork.media.core.model.UiMimeType

interface CroppedPreviewUseCase {
    suspend operator fun invoke(
        uri: Uri,
        key: String,
        px: Int,
        mime: UiMimeType = UiMimeType.Photo
    ): String
}