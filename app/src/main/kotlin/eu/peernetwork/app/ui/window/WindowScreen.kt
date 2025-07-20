package eu.peernetwork.app.ui.window

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignPage
import eu.peernetwork.core.ui.design.compose.DesignPageHeader
import eu.peernetwork.core.ui.design.compose.DesignPageWindowMode
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.wallet.ui.reward.RewardScreen

@Composable
fun WindowScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    mode: DesignPageWindowMode = DesignPageWindowMode.HIDDEN,
    content: @Composable () -> Unit
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
                options = {
                    RewardScreen(component, viewModelStore.get(id))
                },
                action = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chat),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    ) { updatedContent() }
}
