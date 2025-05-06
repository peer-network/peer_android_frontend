package eu.peernetwork.social.ui.member

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.followers.FollowersScreen
import eu.peernetwork.social.ui.followings.FollowingsScreen
import eu.peernetwork.social.ui.peers.PeersScreen
import eu.peernetwork.social.ui.renderder.BlogRenderer
import eu.peernetwork.social.ui.renderder.UserRenderer

sealed class BottomSheetType {
    object FOLLOWERS : BottomSheetType()
    object FOLLOWING : BottomSheetType()
    object PEERS : BottomSheetType()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen(
    id: String,
    limit: Int,
    type: UserRenderer.Type,
    onSettings: () -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Member.Builder::class.java).build(context)
    }
    val userState = remember { mutableStateOf(false) }
    val refreshing = remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf(false) }
    var bottomSheetType by remember { mutableStateOf<BottomSheetType?>(null) }

    val bottomSheetContent: @Composable (State<Boolean>) -> Unit = {
        when (bottomSheetType) {
            BottomSheetType.FOLLOWERS -> FollowersScreen(
                userId = id,
                onDismiss = { showSheet.value = false },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                postLimit = limit,
            )
            BottomSheetType.FOLLOWING -> FollowingsScreen(
                userId = id,
                onDismiss = { showSheet.value = false },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                postLimit = limit,
            )
            BottomSheetType.PEERS -> PeersScreen(
                userId = id,
                onDismiss = { showSheet.value = false },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                postLimit = limit,
            )
            null -> {}
        }
    }

    Box(Modifier.fillMaxSize()) {
        MemberScreen(
            onRefresh = {
                refreshing.value = true
                userState.value = true
            },
            header = { scrollState ->
                FollowButton(
                    viewModelStoreOwner = viewModelStoreOwner,
                    provider = provider
                ) { onFollow, error, success ->
                    component.userRenderer()(
                        modifier = Modifier,
                        UserRenderer.Spec(
                            id,
                            userState,
                            viewModelStoreOwner,
                            type,
                            onSettings,
                            {
                                FollowButtonStateless(
                                    isFollowing = success?.followings?.get(id) ?: it.first,
                                    error = error,
                                    initiallyFollowedBy = it.second,
                                    onClick = { onFollow(id) }
                                )
                            },
                            { sheetType ->
                                bottomSheetType = when (sheetType) {
                                    0 -> BottomSheetType.FOLLOWERS
                                    1 -> BottomSheetType.FOLLOWING
                                    2 -> BottomSheetType.PEERS
                                    else -> null
                                }
                                showSheet.value = bottomSheetType != null
                            }
                        )
                    )
                }
            }
        ) {
            component.blogRenderer()(
                modifier = Modifier,
                BlogRenderer.Spec(
                    id,
                    refreshing,
                    limit,
                    BlogRenderer.Type.UNSPECIFIED,
                    viewModelStoreOwner
                )
            )
        }
        DesignBottomSheet(
            onDismissRequest = {
                showSheet.value = false
            },
            tag = "traki",
            showSheet = showSheet,
            sheetPeekHeight = 500.dp,
            modifier = Modifier
                .defaultMinSize(minHeight = 500.dp),
            content = bottomSheetContent
        )
    }
}

@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    header: @Composable (State<Float>) -> Unit,
    content: @Composable () -> Unit
) {
    val state = remember { mutableStateOf(DesignStatefulScaffoldState.Success(Unit)) }
    DesignRefreshableScaffold<Unit>(
        state = state,
        modifier = Modifier.fillMaxSize(),
        onRefresh = onRefresh
    ) {
        DesignScaffold(
            modifier = modifier.fillMaxSize(),
            header = header,
        ) { state -> content() }
    }
}
