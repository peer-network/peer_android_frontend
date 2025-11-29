package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignBottomSheet
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.social.ui.followers.FollowersScreen
import eu.peernetwork.social.ui.followings.FollowingsScreen
import eu.peernetwork.social.ui.model.UiMember
import eu.peernetwork.social.ui.peers.PeersScreen

@Composable
fun ProfileSheet(
    id: String,
    limit: Int,
    state: MutableState<ConnectionStatus?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val sheetState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    DesignBottomSheet(
        state = showSheet,
        onDismiss = { state.value = null },
        peekHeight = 400.dp,
    ) {
        DesignStream(sheetState) { status ->
            Box(modifier = Modifier.statusBarsPadding()) {
                when (status.value) {
                    ConnectionStatus.FOLLOWER -> FollowersScreen(
                        userId = id,
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = {
                            state.value = null
                        }
                    )
                    ConnectionStatus.FOLLOWING -> FollowingsScreen(
                        userId = id,
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = {
                            state.value = null
                        }
                    )
                    ConnectionStatus.PEER -> PeersScreen(
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = {
                            state.value = null
                        }
                    )
                }
            }
        }
    }
}
