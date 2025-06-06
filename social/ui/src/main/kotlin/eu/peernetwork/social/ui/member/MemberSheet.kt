package eu.peernetwork.social.ui.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.social.ui.followers.FollowersScreen
import eu.peernetwork.social.ui.followings.FollowingsScreen
import eu.peernetwork.social.ui.model.UiMember
import eu.peernetwork.social.ui.peers.PeersScreen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberSheet(
    id: String,
    state: MutableState<Boolean>,
    limit: Int,
    status: MutableState<ConnectionStatus?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit
) {
    DesignBottomSheet(
        onDismissRequest = { state.value = false },
        tag = "MemberBottomSheet",
        showSheet = state,
        background = {
            DesignOverlayBackground(
                state = it,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .6f))
            )
        },
        content = {
            status.value?.let {
                when (it) {
                    ConnectionStatus.FOLLOWER -> FollowersScreen(
                        userId = id,
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = onClick
                    )
                    ConnectionStatus.FOLLOWING -> FollowingsScreen(
                        userId = id,
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = onClick
                    )
                    ConnectionStatus.PEER -> PeersScreen(
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = onClick
                    )
                }
            }
        }
    )
}
