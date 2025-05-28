package eu.peernetwork.messaging.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost

@Composable
fun ChatScreen(
    show: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    Column(modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        if (show) {
            DesignTitleBarHost("ChatScreen") {
                titleBar {
                    DesignTitle {
                        Text(stringResource(R.string.chat_label))
                    }
                }
            }
        }
    }
}
