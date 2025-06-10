package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun ConfirmationScreen(
    show: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onConfirm: () -> Unit,
) {
}
