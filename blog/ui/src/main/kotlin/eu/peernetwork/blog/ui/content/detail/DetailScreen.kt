package eu.peernetwork.blog.ui.content.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.mapper.mapToVideo
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoThumbnail

@Composable
fun DetailScreen(
    id: String,
    userId: String,
    limit: Int,
    event: UiPostListener,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val component = remember {
        provider.builder(Detail.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when (state) {
            is DetailViewModel.State.Default -> {
                DesignSceneState.Default
            }
            is DetailViewModel.State.Loading -> DesignSceneState.Loading
            is DetailViewModel.State.Success -> {
                DesignSceneState.Success((state as DetailViewModel.State.Success).post)
            }
            is DetailViewModel.State.Error -> {
                DesignSceneState.Error((state as DetailViewModel.State.Error).error)
            }
        }
    } }
    val length = remember { mutableLongStateOf(0L) }
    val current = remember { mutableIntStateOf(0) }
    val isPlaying = remember { mutableStateOf(true) }
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
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                DetailPage(
                    userId = userId,
                    state = derivedState,
                    event = event,
                    engagement = engagement,
                    moderation = moderation,
                    connection = connection,
                    audio = { post, expanded ->
                        val path by remember { derivedStateOf { post.media.first().path } }
                        component.audioPlayer().Thumbnail(
                            path = path,
                            hasControls = !expanded,
                            position = current.intValue,
                            enable = isPlaying,
                            isActive = isPlaying,
                            length = length,
                            current = current,
                            modifier = Modifier
                        )
                    },
                    video = { post ->
                        val videoPost = post.mapToVideo()
                        val postThumbnail = remember { derivedStateOf { thumbnail.value[videoPost.media] } }
                        DesignThumbnail(
                            enable = isPlaying,
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
                    image = { path, ratio ->
                        component.imageView()(
                            modifier = Modifier,
                            spec = ImageView.Spec(
                                url = path,
                                ratio = ratio,
                                contentScale = ContentScale.Crop,
                                blur = 500f,
                            )
                        )
                        component.imageView()(
                            modifier = Modifier,
                            spec = ImageView.Spec(
                                url = path,
                                ratio = ratio
                            )
                        )
                    }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        if (state is DetailViewModel.State.Default) {
            viewModel.load(id)
        }
    }
}
