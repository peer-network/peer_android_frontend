package eu.peernetwork.media.core.usecase

import java.io.InputStream
import eu.peernetwork.media.core.model.UiMimeType

interface DecodeAndCacheThumbnailUsecase {
    data class Params(
        val key: String,
        val stream: InputStream,
        val boxPx: Int,
        val mime: UiMimeType
    )

    suspend operator fun invoke(param: Params)
}
