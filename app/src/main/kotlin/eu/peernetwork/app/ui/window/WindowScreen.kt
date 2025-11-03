package eu.peernetwork.app.ui.window

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignPage
import eu.peernetwork.core.ui.design.material.DesignPageHeader
import eu.peernetwork.core.ui.design.material.DesignPageWindowMode
import eu.peernetwork.core.ui.design.material.DesignTitleBar
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.LightScheme
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.reward.RewardScreen

@Composable
fun WindowScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    mode: DesignPageWindowMode = DesignPageWindowMode.HIDDEN,
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Window.Builder::class.java).build(context)
    }
    val updatedContent by rememberUpdatedState(content)
    DesignPage(
        mode,
        header = {
            DesignPageHeader(
                options = { RewardScreen(component, viewModelStoreOwner) },
                action = {
                    if (mode != DesignPageWindowMode.HIDDEN) {
                        IconButton(onClick = onCancel) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    ) { updatedContent() }
}

@Composable
fun WindowTitle(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onCancel: () -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Window.Builder::class.java).build(context)
    }
    PeerTheme(colorScheme = LightScheme) {
        DesignTitleBar {
            DesignPageHeader(
                options = { RewardScreen(component, viewModelStoreOwner) },
                action = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cancel),
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
