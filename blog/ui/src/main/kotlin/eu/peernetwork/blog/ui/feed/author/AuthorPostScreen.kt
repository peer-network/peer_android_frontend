package eu.peernetwork.blog.ui.feed.author

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.content.timeline.TimelineScreen
import eu.peernetwork.blog.ui.mapper.getAspectRatio
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignError
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignLoader
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AuthorPostScreen(
    author: String,
    types: Set<Content.Type>,
    postLimit: Int,
    status: State<Boolean>,
    requireUpdate: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostListener,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable (Triple<String, Boolean, Boolean>) -> Unit = {},
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember { provider.builder(AuthorPost.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = AuthorPostViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                AuthorPostViewModel.State.Empty -> DesignStreamState.Default
                AuthorPostViewModel.State.Loading -> DesignStreamState.Loading
                is AuthorPostViewModel.State.Success -> DesignStreamState.Success(
                    (state as AuthorPostViewModel.State.Success).content
                )
                is AuthorPostViewModel.State.Error -> DesignStreamState.Error(
                    (state as AuthorPostViewModel.State.Error).error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    val pause = remember { mutableStateOf(false) }
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    pause.value = false
                }
                Lifecycle.Event.ON_STOP -> {
                    pause.value = true
                }
                else -> Unit
            }
        }
    }
    PostScreen(
        id = author,
        limit = postLimit,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = {}
    ) { c, p ->
        val updatedConnection by rememberUpdatedState(connection)
        DesignStream(derivedState) {
            val lazyPagingItems = it.value.collectAsLazyPagingItems()
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index -> lazyPagingItems[index]?.id?.let { "$it;$index" } ?: index }
                ) { index ->
                    lazyPagingItems[index]?.let { post ->
                        PostScreen(
                            type = post.type,
                            pinnedBy = null,
                            model = post.mapToDetail(),
                            media = post.media,
                            engagement = post.mapToEngagement(),
                            connection = {
                                updatedConnection(
                                    Triple(
                                        post.author.id,
                                        post.author.isfollowing,
                                        post.author.isfollowed
                                    )
                                )
                            },
                            video = {},
                            image = { path ->
                                component.imageView()(
                                    modifier = Modifier,
                                    spec = ImageView.Spec(
                                        url = path,
                                        ratio = post.aspectRatio,
                                        contentScale = ContentScale.Crop,
                                        blur = 500f,
                                    )
                                )
                                component.imageView()(
                                    modifier = Modifier,
                                    spec = ImageView.Spec(
                                        url = path,
                                        ratio = post.aspectRatio
                                    )
                                )
                            }
                        ) {

                        }
                    }
                }
                item(key = author) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (lazyPagingItems.loadState.append is LoadState.Loading) {
                            DesignLoader { PostPlaceholder(contentPaddingValues = PaddingValues(16.dp)) }
                        }
                        Box(modifier = Modifier.fillMaxWidth()
                            .height(48.dp))
                    }
                }
            }
        }
    }
    LaunchedEffect(requireUpdate.value) {
        if (requireUpdate.value || state is AuthorPostViewModel.State.Empty) {
            viewModel.load(
                author,
                types,
                Pageable(0, postLimit)
            )
            requireUpdate.value = false
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
