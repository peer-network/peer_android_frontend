package eu.peernetwork.app.ui.messaging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.messaging.ui.chat.ChatScreen

@Composable
fun MessagingScreen(
    currentUserId: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Messaging.Builder::class.java).build(context)
    }
    var selectedChatId by rememberSaveable { mutableStateOf<String?>(null) }

    if (selectedChatId == null) {
        ChatScreen()
    } else {

    }
}