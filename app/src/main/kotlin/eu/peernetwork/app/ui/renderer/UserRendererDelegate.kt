package eu.peernetwork.app.ui.renderer

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.renderder.UserRenderer
import eu.peernetwork.user.ui.account.AccountScreen
import eu.peernetwork.user.ui.user.UserScreen

class UserRendererDelegate(val provider: UiComponentProvider) : UserRenderer {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: UserRenderer.Spec
    ) {
        if (spec.type == UserRenderer.Type.ACCOUNT) {
            AccountScreen(
                loadState = spec.state,
                onSettings = spec.onSettings,
                onClick = spec.onClick,
                provider = provider,
                viewModelStoreOwner = spec.viewModelStoreOwner,
                modifier = Modifier.Companion.padding(bottom = 8.dp)
                    .padding(end = 16.dp, start = 24.dp)
            )
        } else {
            UserScreen(
                id = spec.id,
                loadState = spec.state,
                onFollow = spec.onFollow,
                onClick = spec.onClick,
                provider = provider,
                viewModelStoreOwner = spec.viewModelStoreOwner,
                modifier = Modifier.Companion.padding(bottom = 8.dp)
                    .padding(end = 16.dp, start = 24.dp)
            )
        }
    }
}