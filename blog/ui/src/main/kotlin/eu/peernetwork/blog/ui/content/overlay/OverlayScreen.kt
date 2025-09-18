package eu.peernetwork.blog.ui.content.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.RefreshableContentScaffold
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.mapper.query
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoPlayer
import kotlinx.coroutines.flow.Flow

@Composable
fun OverlayScreen(
    id: String,
    limit: Int,
    position: Int,
    enabled: Boolean,
    event: UiPostListener,
    state: State<DesignSceneState<Flow<PagingData<UiPost>>>>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onView: (String) -> Unit,
    onRefresh: () -> Unit,
    header: @Composable () -> Unit,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val component = remember {
        provider.builder(Overlay.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = OverlayViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val handleOnView by rememberUpdatedState(onView)
    val updatedConnection by rememberUpdatedState(connection)
    val length = remember { mutableLongStateOf(0L) }
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
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
                resource = component.resource(),
                onRefresh = onRefresh
            ) { state, list ->
                if (list.value.loadState.refresh is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    OverlayPager(
                        position = position,
                        enabled = enabled,
                        engagement = engagement,
                        moderation = moderation,
                        lazyPagingItems = list,
                        event = event,
                        header = header,
                        connection = { post, connection ->
                            if (id != post.author.id) {
                                updatedConnection(
                                    Triple(
                                        post.author.id,
                                        post.author.isfollowing,
                                        post.author.isfollowed
                                    )
                                )
                            }
                        },
                        indicator = { state, items -> PhotoIndicator(state, items) },
                        progress = { type, progress ->
                            component.videoPlayer().Controller(
                                modifier = Modifier.padding(horizontal = 24.dp)
                                    .padding(end = 8.dp)
                                    .navigationBarsPadding(),
                                progress = progress,
                                length = length
                            )
                        },
                        background = { post ->
                            val path = "${post.media}${UiMimeType.Video.query()}"
                            val bitmap = remember { derivedStateOf { thumbnail.value[path] } }
                            DesignThumbnail(post.media, bitmap) {
                                viewModel.videoBackground(
                                    media = path,
                                    aspectRatio = post.aspectRatio,
                                    width = configuration.screenWidthDp,
                                    height = configuration.screenHeightDp,
                                    fit = true
                                )
                            }
                        },
                        audio = { post, state, progress ->
                            component.audioPlayer()(
                                Modifier,
                                spec = AudioPlayer.Spec(
                                    path = post.media.first().path,
                                    length = length,
                                    modifier = Modifier,
                                    progress = progress,
                                    enabled = state
                                )
                            )
                            LaunchedEffect(Unit) {
                                if (!post.isViewed) {
                                    handleOnView(post.id)
                                }
                            }
                        },
                        video = { post, shouldPlay, progress ->
                            component.videoPlayer()(
                                Modifier,
                                spec = VideoPlayer.Spec(
                                    url = post.media,
                                    ratio = post.aspectRatio,
                                    progress = progress,
                                    length = length,
                                    enabled = shouldPlay,
                                )
                            )
                            LaunchedEffect(Unit) {
                                if (!post.isViewed) {
                                    handleOnView(post.id)
                                }
                            }
                        }
                    ) { post, path ->
                        component.imageView()(
                            Modifier,
                            spec = ImageView.Spec(
                                url = path,
                                ratio = null,
                                contentScale = ContentScale.Crop,
                                blur = 500f,
                            )
                        )
                        component.imageView()(
                            Modifier,
                            spec = ImageView.Spec(path, post.aspectRatio, zoomable = true)
                        )
                        LaunchedEffect(Unit) {
                            if (!post.isViewed) {
                                handleOnView(post.id)
                            }
                        }
                    }
                }
            }
        }
    }
}
