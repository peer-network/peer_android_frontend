package eu.peernetwork.app.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun WelcomeScreen(
    referral: String? = null,
    provider: UiComponentProvider,
    onBrowse: (String) -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Welcome.Builder::class.java).build(context)
    }
    WelcomeScaffold {
        WelcomeNavigation(
            referral = referral,
            provider = component,
            onBrowse = onBrowse
        )
    }
}
