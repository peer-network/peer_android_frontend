package eu.peernetwork.social.ui.followings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.*
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.compose.Peer
import eu.peernetwork.social.ui.compose.SearchItemSkeleton
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.model.UiMember

@Composable
fun FollowingsScreen(
    userId: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit
) {
    ConnectionScreen(provider = provider, viewModelStoreOwner = viewModelStoreOwner) { controller ->

        val context = LocalContext.current
        val component = remember {
            provider.builder(Followings.Builder::class.java).build(context)
        }

        val viewModel: FollowingsViewModel = viewModel(
            viewModelStoreOwner = viewModelStoreOwner,
            factory = component.viewModelFactory()
        )

        val state by viewModel.state.collectAsState()
        val connectionMap by controller.observe().collectAsState()

        val derivedState = remember {
            derivedStateOf {
                when (state) {
                    FollowingsViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                    FollowingsViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                    is FollowingsViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                        (state as FollowingsViewModel.State.Success).content
                    )
                    is FollowingsViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                        (state as FollowingsViewModel.State.Error).error
                    )
                }
            }
        }

        DesignPagingScaffold<UiMember>(
            state = derivedState,
            onRefresh = {
                viewModel.followers(userId, pageable = Pageable(offset = 0, limit = postLimit))
            },
            modifier = Modifier.fillMaxSize(),
            placeholder = { SearchItemSkeleton(modifier = Modifier.padding(horizontal = 16.dp)) },
            errorContent = { error, refresh ->
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DesignErrorLabel(refresh, error, component.resource(), PaddingValues(horizontal = 16.dp))
                }
            }
        ) { _, lazyPagingItems ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(lazyPagingItems.itemCount) { index ->
                    lazyPagingItems[index]?.let { member ->
                        val isFollowing = connectionMap[member.id] == true
                        val isFollowed = connectionMap[member.id] == false

                        Peer(
                            member = member,
                            onClick = onClick,
                            action = {
                                ConnectionScreen(
                                    isFollowing = isFollowing,
                                    isFollowed = isFollowed,
                                    onClick = { controller(member.id, !isFollowing) }
                                )
                            }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(56.dp)) }
            }
        }

        DisposableEffect(Unit) {
            onDispose { viewModel.reset() }
        }
    }
}


