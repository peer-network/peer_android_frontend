package eu.peernetwork.social.ui.member

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.social.ui.followers.FollowersScreen
import eu.peernetwork.social.ui.followings.FollowingsScreen
import eu.peernetwork.social.ui.peers.PeersScreen

@Composable
fun MemberSheet(
    id: String,
    limit: Int,
    status: ConnectionStatus,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    when (status) {
        ConnectionStatus.FOLLOW -> FollowersScreen(
            userId = id,
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner,
            postLimit = limit,
        )
        ConnectionStatus.FOLLOWING -> FollowingsScreen(
            userId = id,
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner,
            postLimit = limit,
        )
        ConnectionStatus.PEER -> PeersScreen(
            userId = id,
            provider = provider,
            viewModelStoreOwner = viewModelStoreOwner,
            postLimit = limit,
        )
    }
}
