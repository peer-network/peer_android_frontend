package eu.peernetwork.media.core.model

import androidx.compose.runtime.Stable

@Stable
sealed class UiAttachment(
    val media: UiMimeType,
    val files: List<UiFile>
) {
    @Stable
    data class File(
        val type: UiMimeType,
        val uris: List<UiFile>
    ) : UiAttachment(type, uris)

    @Stable
    data object Text : UiAttachment(
        media = UiMimeType.Text,
        emptyList()
    )
}
