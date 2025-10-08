package eu.peernetwork.blog.ui.content.overlay

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.mapper.query
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoPlayer

@Composable
fun OverlayPage(
    userId: String,
    limit: Int,
    status: Boolean,
    event: UiPostListener,
    state: State<UiPost>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onView: (String) -> Unit,
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
    val current = remember { mutableIntStateOf(0) }
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val isActive = remember { mutableStateOf(true) }
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
            OverlayPreview(
                page = current.intValue,
                post = state.value,
                enabled = status,
                isActive = isActive,
                engagement = engagement,
                moderation = moderation,
                event = event,
                current = current,
                header = header,
                connection = { post, connection ->
                    if (userId != post.author.id) {
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
                audio = { post, state, index, current, progress ->
                    component.audioPlayer()(
                        Modifier,
                        spec = AudioPlayer.Spec(
                            path = post.media.first().path,
                            length = length,
                            modifier = Modifier,
                            progress = progress,
                            enabled = state,
                            current = current,
                            position = index
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
                },
                image = { post, path ->
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
            )
        }
    }
}
