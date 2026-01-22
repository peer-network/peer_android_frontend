package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.social.ui.connection.ConnectionButton
import eu.peernetwork.social.ui.connection.ConnectionInteractor.Companion.LocalConnectionInteractor
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.user.ui.user.UserMetric
import eu.peernetwork.user.ui.user.UserScreen

@Composable
fun ProfileDetail(
    id: String,
    connection: MutableState<ConnectionStatus?>,
    timestamp: State<Long>,
    onBlock: () -> Unit = {},
    onSettings: () -> Unit = {},
    onMenuClicked: () -> Unit = {},
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) {
        val controller = LocalConnectionInteractor.current
        val connectionState by controller.observe().collectAsStateWithLifecycle()
        UserScreen(
            id = id,
            timestamp = timestamp,
            connection = {
                ConnectionButton(
                    isFollowed = it.second,
                    isFollowing = connectionState.getOrDefault(
                        key = id,
                        defaultValue = it.first
                    ),
                    onClick = { state ->
                        controller.invoke(id, !state)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = { sheetType ->
                connection.value = when (sheetType) {
                    UserMetric.FOLLOWER -> ConnectionStatus.FOLLOWER
                    UserMetric.FOLLOWING -> ConnectionStatus.FOLLOWING
                    UserMetric.PEER -> ConnectionStatus.PEER
                    else -> null
                }
            },
            onSettings = onSettings,
            onMenuClicked = onMenuClicked,
            onBlock = onBlock,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
