package eu.peernetwork.user.ui.v2.password.reset

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun ResetScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    ResetPage(true) {}
}
