package eu.peernetwork.user.ui.v2.referral

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ReferralScreen(
    referral: String? = null,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onRegister: (String) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Referral.Builder::class.java).build(context)
    }
    ReferralPage(
        onRequestReferral = {},
        onVerify = onRegister
    )
}
