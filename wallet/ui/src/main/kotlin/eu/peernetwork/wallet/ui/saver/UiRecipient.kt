package eu.peernetwork.wallet.ui.saver

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import eu.peernetwork.wallet.ui.model.UiRecipient

val UiRecipientSaver: Saver<MutableState<UiRecipient?>, *> = Saver(
    save = { it.value?.let { listOf(it.id, it.slug, it.username, it.imageUrl) } },
    restore = {
        val list = it
        mutableStateOf(
            UiRecipient(
                id = list[0],
                slug = list[1],
                username = list[2],
                imageUrl = list[3],
            )
        )
    }
)
