package eu.peernetwork.social.ui.followers

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
fun FollowersScreen(
    userId: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit
) {
    ConnectionScreen(provider = provider, viewModelStoreOwner = viewModelStoreOwner) { controller ->

        val context = LocalContext.current
        val component = remember {
            provider.builder(Followers.Builder::class.java).build(context)
        }
        val viewModel = viewModel(
            modelClass = FollowersViewModel::class.java,
            viewModelStoreOwner = viewModelStoreOwner,
            factory = component.viewModelFactory()
        )

        val state by viewModel.state.collectAsState()
        val connectionMap by controller.observe().collectAsState()

        val derivedState = remember {
            derivedStateOf {
                when (state) {
                    FollowersViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                    FollowersViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                    is FollowersViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                        (state as FollowersViewModel.State.Success).content
                    )
                    is FollowersViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                        (state as FollowersViewModel.State.Error).error
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
                        val connection = connectionMap[member.id]
                        val isFollowing = connection == true
                        val isFollowed = connection == true

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



