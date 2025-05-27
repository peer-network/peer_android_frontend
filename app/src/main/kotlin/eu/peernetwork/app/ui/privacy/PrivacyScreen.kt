package eu.peernetwork.app.ui.privacy

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.user.ui.R

@Composable
fun PrivacyScreen(onFinish: () -> Unit) {
    DesignTitleBarHost("PasswordRequestScreen", onFinish) {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.password_recovery_label))
            }
        }
    }
}
