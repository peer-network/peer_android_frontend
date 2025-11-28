package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignCollapsibleBottomSheet
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.social.ui.followers.FollowersScreen
import eu.peernetwork.social.ui.followings.FollowingsScreen
import eu.peernetwork.social.ui.model.UiMember
import eu.peernetwork.social.ui.peers.PeersScreen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileSheet(
    id: String,
    state: MutableState<Boolean>,
    limit: Int,
    status: MutableState<ConnectionStatus?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit
) {
    val handleOnClick by rememberUpdatedState(onClick)
    var selectedProfile by remember { mutableStateOf<UiMember?>(null) }
    DesignCollapsibleBottomSheet(
        onDismiss = {
            selectedProfile?.let { handleOnClick(it) }
            state.value = false
            selectedProfile = null },
        peekHeight = 400.dp,
        state = state,
        content = {
            Box(modifier = Modifier.statusBarsPadding()) {
                val connection = remember { mutableStateOf(status.value) }
                connection.value?.let {
                    when (it) {
                        ConnectionStatus.FOLLOWER -> FollowersScreen(
                            userId = id,
                            provider = provider,
                            viewModelStoreOwner = viewModelStoreOwner,
                            postLimit = limit,
                            onClick = {
                                selectedProfile = it
                                state.value = false
                            }
                        )
                        ConnectionStatus.FOLLOWING -> FollowingsScreen(
                            userId = id,
                            provider = provider,
                            viewModelStoreOwner = viewModelStoreOwner,
                            postLimit = limit,
                            onClick = {
                                selectedProfile = it
                                state.value = false
                            }
                        )
                        ConnectionStatus.PEER -> PeersScreen(
                            provider = provider,
                            viewModelStoreOwner = viewModelStoreOwner,
                            postLimit = limit,
                            onClick = {
                                selectedProfile = it
                                state.value = false
                            }
                        )
                    }
                }
            }
        }
    )
}
