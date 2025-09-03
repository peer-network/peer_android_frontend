package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.compose.RefreshableContentScaffold
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.mapper.mapToVideo
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun PhotoScreen(
    id: String,
    status: State<Boolean>,
    postLimit: Int,
    category: Category,
    criteria: Criteria? = null,
    event: UiPostEvent,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    requireUpdate: MutableState<Boolean>,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
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
            RefreshableContentScaffold(
                state = derivedState,
                resource = component.resource(),
                onRefresh = { viewModel.load(Pageable(0, postLimit), category, criteria) }
            ) { state, list ->
                PhotoListing(
                    id = id,
                    current = current,
                    viewModel = viewModel,
                    listState = listState,
                    lazyPagingItems = list,
                    engagement = engagement,
                    moderation = moderation,
                    event = event,
                    connection = connection,
                    audio = { post, index, position ->
                        val path by remember { derivedStateOf { post.media.first().path } }
                        val enable = remember { derivedStateOf { index == position.value } }
                        val progress = remember { mutableFloatStateOf(0f) }
                        component.audioPlayer().Thumbnail(
                            path = path,
                            position = index,
                            enable = enable,
                            pause = pause,
                            length = length,
                            progress = progress,
                            current = current,
                            modifier = Modifier
                        )
                    },
                    video = { post, index, position ->
                        val videoPost = post.mapToVideo()
                        val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
                        val isPlaying = remember { derivedStateOf { index == position.value && status.value } }
                        val postThumbnail = remember { derivedStateOf { thumbnail.value[videoPost.media] } }
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
                LaunchedEffect(requireUpdate.value) {
                    if (requireUpdate.value) {
                        list.value.refresh()
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                        requireUpdate.value = false
                    }
                }
            }
        }
    }
    LaunchedEffect(category, criteria) {
        if (category != viewModel.lastCategory) {
            viewModel.load(Pageable(0, postLimit), category, criteria)
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
