package eu.peernetwork.media.ui.editor.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun VideoScreen(
    type: UiMimeType,
    directory: MutableState<String?>,
    attachment: MutableState<UiAttachment>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    isEdit: Boolean = false
) {
    if (isEdit) {

        println("EDIT MODE ON!")
    }


}
