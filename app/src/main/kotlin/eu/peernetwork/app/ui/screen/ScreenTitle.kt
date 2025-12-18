package eu.peernetwork.app.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignPageHeader
import eu.peernetwork.core.ui.design.material.DesignTitleBar
import eu.peernetwork.core.ui.design.material.DesignTitleBarRegistry
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.LightScheme
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.reward.RewardScreen

@Composable
fun DesignTitleBarRegistry.ScreenTitle(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onCancel: () -> Unit = {}
) {
    DesignPageHeader(
        options = { RewardScreen(provider, viewModelStoreOwner) },
        action = {
            IconButton(onClick = onCancel) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun ScreenTitle(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onCancel: () -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Screen.Builder::class.java).build(context)
    }
    PeerTheme(colorScheme = LightScheme) {
        DesignTitleBar {
            DesignPageHeader(
                options = { RewardScreen(component, viewModelStoreOwner) },
                action = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
