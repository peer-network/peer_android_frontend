package eu.peernetwork.user.ui.v2.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun RegistrationScreen(
    referral: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: androidx.lifecycle.ViewModelStoreOwner,
    onLogin: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Registration.Builder::class.java).build(context)
    }
    RegistrationPage(onLogin = onLogin) {

    }
}
