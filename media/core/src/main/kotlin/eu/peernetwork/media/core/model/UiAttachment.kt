package eu.peernetwork.media.core.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed class UiAttachment(
    val media: UiMimeType,
    val files: ImmutableList<UiFile>
) {
    @Immutable
    data class File(
        val type: UiMimeType,
        val uris: ImmutableList<UiFile>,
        val cover: Uri? = null
    ) : UiAttachment(type, uris)

    @Immutable
    data object Text : UiAttachment(
        media = UiMimeType.Text,
        files = persistentListOf()
    )
}
