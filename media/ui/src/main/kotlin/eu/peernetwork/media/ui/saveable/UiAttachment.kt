package eu.peernetwork.media.ui.saveable

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.collections.immutable.toImmutableList
import androidx.core.net.toUri

val UiAttachmentSaver: Saver<MutableState<UiAttachment>, *> = Saver(
    save = {
        it.value.let { attachment ->
            when (attachment) {
                is UiAttachment.File -> {
                    listOf(
                        "FILE",
                        UiMimeType.TYPES.indexOf(attachment.type),
                        attachment.uris.map { it.uri.toString() to it.thumbnail }
                    )
                }
                UiAttachment.Text -> listOf("TEXT")
            }
        }
    },
    restore = { saved ->
        when (saved[0]) {
            "FILE" -> mutableStateOf(UiAttachment.File(
                type = UiMimeTypeSaver.restore(saved[1] as Int) ?: UiMimeType.Photo,
                uris = (saved[2] as List<Pair<String, String>>).map { (uriStr, thumbnail) ->
                    UiFile(uriStr.toUri(), thumbnail)
                }.toImmutableList()
            ))
            "TEXT" -> mutableStateOf(UiAttachment.Text)
            else -> throw IllegalArgumentException("Unknown type")
        }
    }
)

val UiMimeTypeSaver = Saver<UiMimeType, Int>(
    save = { mimeType -> UiMimeType.TYPES.indexOf(mimeType) },
    restore = { index -> UiMimeType.TYPES.getOrNull(index) ?: UiMimeType.Text }
)
