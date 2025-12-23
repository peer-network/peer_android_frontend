package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import eu.peernetwork.social.ui.peers.PeersScreen

sealed interface ProfileSheetState {
    data object Hidden : ProfileSheetState
    data object Visible: ProfileSheetState

    data class Dismissing(val action: () -> Unit): ProfileSheetState
}

@Composable
fun ProfileSheet(
    id: String,
    limit: Int,
    state: MutableState<ConnectionStatus?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (String) -> Unit
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val sheetState = remember { mutableStateOf<ProfileSheetState>(ProfileSheetState.Hidden) }
    val streamState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    val handleOnClick by rememberUpdatedState(onClick)
    DesignBottomSheet(
        state = showSheet,
        onDismiss = {
            if (sheetState.value is ProfileSheetState.Dismissing) {
                (sheetState.value as ProfileSheetState.Dismissing).action.invoke()
            }
            sheetState.value = ProfileSheetState.Hidden
            state.value = null
        },
        peekHeight = 400.dp,
    ) {
        DesignStream(streamState) { status ->
            Box(modifier = Modifier.statusBarsPadding()) {
                when (status.value) {
                    ConnectionStatus.FOLLOWER -> FollowersScreen(
                        userId = id,
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = {
                            sheetState.value = ProfileSheetState.Dismissing {
                                handleOnClick(it)
                            }
                            state.value = null
                        }
                    )
                    ConnectionStatus.FOLLOWING -> FollowingsScreen(
                        userId = id,
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = {
                            sheetState.value = ProfileSheetState.Dismissing {
                                handleOnClick(it)
                            }
                            state.value = null
                        }
                    )
                    ConnectionStatus.PEER -> PeersScreen(
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner,
                        postLimit = limit,
                        onClick = {
                            sheetState.value = ProfileSheetState.Dismissing {
                                handleOnClick(it)
                            }
                            state.value = null
                        }
                    )
                }
            }
        }
    }
    LaunchedEffect(state.value) {
        state.value?.let {
            sheetState.value = ProfileSheetState.Visible
        }
    }
}
