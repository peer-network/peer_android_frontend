package eu.peernetwork.user.ui.v2.password.request

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun RequestScreen(
    email: String?,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onReset: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Request.Builder::class.java).build(context)
    }
    val handleOnReset by rememberUpdatedState(onReset)
    RequestPage(email ?: "") {
        handleOnReset()
    }
}
