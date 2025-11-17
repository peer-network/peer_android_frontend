package eu.peernetwork.ads.ui.boost

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost

@Composable
fun BoostScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    DesignTitleBarHost("BoostScreen$id", {}) {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.boost_label))
            }
        }
    }
}
