package eu.peernetwork.app.ui.profile.v2

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.social.ui.connection.ConnectionButton
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.user.ui.user.UserMetric
import eu.peernetwork.user.ui.user.UserScreen

@Composable
fun ProfileDetail(
    id: String,
    timestamp: State<Long>,
    showSheet: MutableState<Boolean>,
    onSettings: () -> Unit = {},
    onMenuClicked: () -> Unit = {},
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { controller ->
        val connectionState by controller.value.observe().collectAsStateWithLifecycle()
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
                    onClick = { follow ->
                        controller.value.invoke(id, !follow)
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
                showSheet.value = connection.value != null
            },
            onSettings = onSettings,
            onMenuClicked = onMenuClicked,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
