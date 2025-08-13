package eu.peernetwork.social.ui.followers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignErrorLabel
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
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
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                FollowersViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                FollowersViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is FollowersViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as FollowersViewModel.State.Success).content
                    )
                }
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
            Column(modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(8.dp))
                DesignErrorLabel(refresh, error, component.resource(), PaddingValues(horizontal = 16.dp))
            }
        }
    ) { state, lazyPagingItems ->
        ConnectionScreen(provider = provider, viewModelStoreOwner = viewModelStoreOwner) { controller ->
            val connection by controller.value.observe().collectAsState()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(lazyPagingItems.itemCount) { index ->
                    lazyPagingItems[index]?.let { member ->
                        Peer(
                            member = member,
                            onClick = onClick,
                            action = {
                                ConnectionScreen(
                                    isFollowing = connection.getOrDefault(
                                        member.id,
                                        member.isFollowed
                                    ),
                                    isFollowed = member.isFollowing,
                                    onClick = { follow ->
                                        controller.value.invoke(
                                            member.id,
                                            !follow
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(56.dp)) }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}