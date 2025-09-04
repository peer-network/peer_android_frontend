package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.compose.ContentScaffold
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.mapper.mapToVideo
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoThumbnail

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PhotoScreen(
    author: String,
    types: Set<Content.Type>,
    postLimit: Int,
    status: State<Boolean>,
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostEvent,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember { provider.builder(Photo.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val updatedAt = remember { mutableLongStateOf(lastUpdated.value) }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignSceneState.Default
                PhotoViewModel.State.Loading -> DesignSceneState.Loading
                is PhotoViewModel.State.Success -> DesignSceneState.Success(
                    (state as PhotoViewModel.State.Success).content
                )
                is PhotoViewModel.State.Error -> DesignSceneState.Error(
                    (state as PhotoViewModel.State.Error).error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    val current = remember { mutableIntStateOf(-1) }
    val pause = remember { mutableStateOf(false) }
    val isActive = remember { derivedStateOf { status.value && !pause.value } }
    val length = remember { mutableLongStateOf(0L) }
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
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    EngagementScreen(
        postLimit = postLimit,
        onMentionClick = event::onMentionClick,
        onHashtagClick = event::onHashtagClick,
        onAuthorClick = event::onAuthorClick,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { engagement ->
        ModerationScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { moderation ->
            ContentScaffold(
                state = derivedState,
                resource = component.resource(),
                onRefresh = { viewModel.load(author, types, Pageable(0, postLimit)) },
                modifier = Modifier.fillMaxSize()
            ) { state, list ->
                PhotoListing(
                    author = author,
                    current = current,
                    viewModel = viewModel,
                    listState = listState,
                    lazyPagingItems = list,
                    engagement = engagement,
                    moderation = moderation,
                    event = event,
                    audio = { post, index, position, expanded ->
                        val path by remember { derivedStateOf { post.media.first().path } }
                        val enable = remember { derivedStateOf { index == position.value } }
                        if (expanded) {
                            component.audioPlayer()(
                                modifier = Modifier,
                                spec = AudioPlayer.Spec(
                                    path = path,
                                    position = index,
                                    enable = enable,
                                    isActive = isActive,
                                    length = length,
                                    current = current,
                                    modifier = Modifier
                                )
                            )
                        } else {
                            component.audioPlayer().Thumbnail(
                                path = path,
                                position = index,
                                enable = enable,
                                isActive = isActive,
                                length = length,
                                current = current,
                                modifier = Modifier
                            )
                        }
                    },
                    video = { post, index, position ->
                        val videoPost = post.mapToVideo()
                        val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
                        val isPlaying =
                            remember { derivedStateOf { index == position.value && status.value } }
                        val postThumbnail =
                            remember { derivedStateOf { thumbnail.value[videoPost.media] } }
                        DesignThumbnail(
                            enable = enable,
                            thumbnail = videoPost.media,
                            bitmap = postThumbnail,
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(post.aspectRatio)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            viewModel.videoBackground(
                                it,
                                videoPost.aspectRatio,
                                configuration.screenWidthDp,
                            )
                        }
                        component.videoThumbnail()(
                            Modifier,
                            VideoThumbnail.Spec(
                                videoPost.media,
                                post.aspectRatio,
                                isPlaying,
                                videoPost.resolution
                            )
                        )
                    },
                    image = { path, aspectRatio ->
                        component.imageView()(
                            modifier = Modifier,
                            spec = ImageView.Spec(
                                url = path,
                                ratio = null,
                                contentScale = ContentScale.Crop,
                                blur = 500f,
                            )
                        )
                        component.imageView()(
                            modifier = Modifier,
                            spec = ImageView.Spec(
                                url = path,
                                ratio = aspectRatio
                            )
                        )
                    }
                )
                LaunchedEffect(lastUpdated.value) {
                    if (updatedAt.longValue != lastUpdated.value) {
                        list.value.refresh()
                        updatedAt.longValue = lastUpdated.value
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        if (derivedState.value is DesignSceneState.Default) {
            viewModel.load(author, types, Pageable(0, postLimit))
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
