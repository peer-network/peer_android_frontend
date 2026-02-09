package eu.peernetwork.social.ui.followers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.error
import eu.peernetwork.social.ui.connection.ConnectionButton
import eu.peernetwork.social.ui.connection.ConnectionInteractor.Companion.LocalConnectionInteractor
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun FollowersScreen(
    uuid: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (String) -> Unit
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
                FollowersViewModel.State.Empty -> DesignStreamState.Default
                FollowersViewModel.State.Loading -> DesignStreamState.Loading
                is FollowersViewModel.State.Success -> {
                    DesignStreamState.Success(
                        (state as FollowersViewModel.State.Success).content
                    )
                }
                is FollowersViewModel.State.Error -> DesignStreamState.Error(
                    (state as FollowersViewModel.State.Error).error
                )
            }
        }
    }
    val handleClick by rememberUpdatedState(onClick)
    DesignPagingStream(
        state = derivedState,
        modifier = Modifier.fillMaxSize(),
        loading = { FollowersSkeleton(3) },
        error = { error ->
            FollowersError(
                error = component.resource().error(error.value),
                onRefresh = {
                    viewModel.followers(
                        userId = uuid,
                        pageable = Pageable(offset = 0, limit = postLimit)
                    )
                },
            )
        }
    ) { lazyPagingItems ->
        ConnectionScreen(provider = provider, viewModelStoreOwner = viewModelStoreOwner) {
            val controller = LocalConnectionInteractor.current
            val connection by controller.observe().collectAsState()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(lazyPagingItems.itemCount) { index ->
                    lazyPagingItems[index]?.let { member ->
                        FollowersMask(
                            isAuthor = uuid == member.id,
                            status = member.status,
                            onClick = { handleClick(member.id) },
                            isAccessible = member.isAccessible,
                        ) {
                            FollowersItem(
                                slug = member.slug,
                                username = member.username,
                                imageUrl = member.imageUrl,
                                onClick = { handleClick(member.id) }
                            ) {
                                ConnectionButton(
                                    isFollowing = connection.getOrDefault(
                                        key = member.id,
                                        defaultValue = member.isFollowed
                                    ),
                                    isFollowed = member.isFollowing,
                                    onClick = { follow ->
                                        controller.invoke(member.id, !follow)
                                    },
                                    fontWeight = FontWeight.SemiBold,
                                    minHeight = 32.dp,
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(56.dp)) }
            }
        }
    }
    LaunchedEffect(Unit) {
        if (state is FollowersViewModel.State.Empty) {
            viewModel.followers(uuid, pageable = Pageable(offset = 0, limit = postLimit))
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}
