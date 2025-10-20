package eu.peernetwork.app.ui.welcome

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.DesignTheme

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
    DesignTheme(
        isDarkMode = if (BuildConfig.USE_SYSTEM_THEME) {
            isSystemInDarkTheme()
        } else {
            true
        }
    ) {
        WelcomeScaffold {
            WelcomeNavigation(
                referral = referral,
                provider = component,
                onBrowse = onBrowse
            )
        }
    }
}
