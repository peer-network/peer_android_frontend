package eu.peernetwork.blog.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.engagement.EngagementReaction.Companion.LocalEngagementReaction
import eu.peernetwork.blog.ui.extension.route
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.PostItem
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.extension.builder

@Composable
fun DetailScreen(
    uuid: String,
    username: String,
    imageUrl: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Detail.Component, DetailViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Detail.Builder::class.java).build(context)
    }
    val updatedContent by rememberUpdatedState(content)
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    PostScreen(
        uuid = uuid,
        limit = limit,
        username = username,
        imageUrl = imageUrl,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
    ) {
        DesignScaffold {
            updatedContent(component, viewModel)
        }
    }
}

@Composable
fun DetailScreen(
    id: String,
    uuid: String,
    enable: State<Boolean>,
    selected: MutableIntState,
    component: Detail.Component,
    viewModel: DetailViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                DetailViewModel.State.Default -> DesignStreamState.Default
                DetailViewModel.State.Loading -> DesignStreamState.Loading
                is DetailViewModel.State.Success -> DesignStreamState.Success(
                    (state as DetailViewModel.State.Success).post
                )
                is DetailViewModel.State.Error -> DesignStreamState.Error(
                    (state as DetailViewModel.State.Error).error
                )
            }
        }
    }
    val isPlaying = remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    DesignStream(
        state = derivedState,
        loading = { PostSkeleton() },
        error = { error ->
            error.value.message?.let {
                DetailError(
                    error = component.resource().string(it),
                    onRefresh = { viewModel.load(id) },
                )
            }
        }
    ) { postState ->
        val post = postState.value
        val engagement = LocalEngagementInteractor.current
        val reaction = LocalEngagementReaction.current
        val navigator = LocalPostNavigator.current
        PostItem(
            type = post.type,
            pinnedBy = post.pinnedBy,
            model = post.mapToDetail(),
            asset = post.asset,
            onMenu = { showSheet.value = post },
            onClick = { selected.intValue = 0 },
            onContentClick = { type, value ->
                navigator.navigate(type.route(value))
            },
            onAuthorClick = {
                navigator.navigate(
                    route = PostNavigator.Route.Profile(post.author.id)
                )
            },
            engagement = {
                EngagementReaction(
                    post = post,
                    state = engagement.observe()
                ) { reaction(post, it) }
            },
            connection = {
                if (uuid != post.author.id) {
                    component.postUserFollow()(
                        modifier = Modifier,
                        PostUserConnection.Spec(
                            id = post.author.id,
                            isFollowing = post.author.following,
                            isFollowed = post.author.followed,
                        )
                    )
                }
            },
            content = { path, expanded ->
                PostMedia(
                    type = post.type,
                    path = path,
                    expanded = expanded,
                    avatar = post.author.imageUrl,
                    ratio = post.asset.ratio,
                    enable = enable,
                    isPlaying = isPlaying,
                ) {}
            }
        )
    }
    LaunchedEffect(Unit) {
        if (state is DetailViewModel.State.Default) {
            viewModel.load(id)
        }
    }
}
