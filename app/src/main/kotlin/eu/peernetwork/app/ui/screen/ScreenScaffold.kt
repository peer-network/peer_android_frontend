package eu.peernetwork.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignPage
import eu.peernetwork.core.ui.design.material.DesignTitleBar
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ScreenScaffold(
    isModal: Boolean = false,
    expanded: Boolean = true,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Screen.Builder::class.java).build(context)
    }
    val updatedContent by rememberUpdatedState(content)
    if (isModal && expanded) {
        DesignTitleBar {
            Box {
                updatedContent()
            }
        }
    } else if (isModal) {
        DesignPage(
            header = {
                ScreenTitle(
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    onCancel = onCancel
                )
            },
            footer = {}
        ) { updatedContent() }
    } else {
        updatedContent()
    }
}
