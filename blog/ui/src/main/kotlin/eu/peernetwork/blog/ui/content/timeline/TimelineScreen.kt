package eu.peernetwork.blog.ui.content.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.EmptyFeed
import eu.peernetwork.blog.ui.compose.RefreshableContentScaffold
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.mapper.mapToVideo
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.flow.Flow

@Composable
fun TimelineScreen(
    id: String,
    state: State<DesignSceneState<Flow<PagingData<UiPost>>>>,
    status: State<Boolean>,
    limit: Int,
    event: UiPostListener,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    listState: LazyListState = rememberLazyListState(),
    onView: (String) -> Unit,
    enable: Boolean = true,
    onRefresh: () -> Unit = {},
    onExplore: (() -> Unit)? = null,
    onLoad: (State<LazyPagingItems<UiPost>>) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val component = remember {
        provider.builder(Timeline.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TimelineViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val pause = remember { mutableStateOf(false) }
    val current = remember { mutableIntStateOf(-1) }
    val length = remember { mutableLongStateOf(0L) }
    val isActive = remember { derivedStateOf { status.value && !pause.value } }
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val handleOnLoad by rememberUpdatedState(onLoad)
    val handleOnRefresh by rememberUpdatedState(onRefresh)
    val handleOnExplore by rememberUpdatedState(onExplore)
    EngagementScreen(
        postLimit = limit,
        onAuthorClick = { event(UiPostListener.Event.Author(it)) },
        onMentionClick = { event(UiPostListener.Event.Mention(it)) },
        onHashtagClick = { event(UiPostListener.Event.Hashtag(it)) },
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { engagement ->
        ModerationScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { moderation ->
            RefreshableContentScaffold(
                state = state,
                enable = enable,
                resource = component.resource(),
                onRefresh = onRefresh,
                empty = {
                    EmptyFeed {
                        handleOnExplore?.invoke() ?: handleOnRefresh()
                    }
                }
            ) { state, list ->
                TimelineList(
                    id = id,
                    current = current,
                    onView = onView,
                    listState = listState,
                    lazyPagingItems = list,
                    engagement = engagement,
                    moderation = moderation,
                    event = event,
                    connection = connection,
                    audio = { post, index, position, expanded ->
                        val path by remember { derivedStateOf { post.media.first().path } }
                        val isPlaying = remember { derivedStateOf { index == position.value } }
                        component.audioPlayer().Thumbnail(
                            path = path,
                            hasControls = !expanded,
                            position = index,
                            enable = isPlaying,
                            isActive = isActive,
                            length = length,
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(post.aspectRatio)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            viewModel.videoBackground(
                                media = it,
                                aspectRatio = videoPost.aspectRatio,
                                width = configuration.screenWidthDp,
                                height = (configuration.screenWidthDp / videoPost.aspectRatio).toInt()
                            )
                        }
                        component.videoThumbnail()(
                            Modifier,
                            spec = VideoThumbnail.Spec(
                                url = videoPost.media,
                                ratio = post.aspectRatio,
                                isPlaying = isPlaying,
                                resolution = videoPost.resolution
                            )
                        )
                    },
                    image = { path, aspectRatio ->
                        component.imageView()(
                            modifier = Modifier,
                            spec = ImageView.Spec(
                                url = path,
                                ratio = aspectRatio,
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
                LaunchedEffect(Unit) {
                    handleOnLoad(list)
                }
            }
        }
    }
}