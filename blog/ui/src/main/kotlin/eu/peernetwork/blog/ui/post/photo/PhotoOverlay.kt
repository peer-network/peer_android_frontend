package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.RefreshableContentScaffold
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.mapper.query
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoPlayer

@Composable
fun PhotoOverlay(
    author: String,
    types: Set<Content.Type>,
    limit: Int,
    position: Int,
    enabled: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostEvent,
    header: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
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
    val length = remember { mutableLongStateOf(0L) }
    val updatedConnection by rememberUpdatedState(connection)
    EngagementScreen(
        postLimit = limit,
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
                onRefresh = { viewModel.load(author, types, Pageable(0, limit)) }
            ) { state, list ->
                if (list.value.loadState.refresh is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    PhotoPager(
                        position = position,
                        enabled = enabled,
                        engagement = engagement,
                        moderation = moderation,
                        lazyPagingItems = list,
                        event = event,
                        header = header,
                        connection = { post, connection ->
                            if (author != post.author.id) {
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
                        progress = {
                            component.videoPlayer().Controller(
                                modifier = Modifier.padding(horizontal = 24.dp)
                                    .padding(end = 8.dp)
                                    .navigationBarsPadding(),
                                progress = it,
                                length = length
                            )
                        },
                        background = { post ->
                            val path = "${post.media}${UiMimeType.Video.query()}"
                            val bitmap = remember { derivedStateOf { thumbnail.value[path] } }
                            DesignThumbnail(post.media, bitmap) {
                                viewModel.videoBackground(
                                    path,
                                    post.aspectRatio,
                                    configuration.screenWidthDp,
                                    configuration.screenHeightDp,
                                    true
                                )
                            }
                        },
                        audio = { post, state, active -> },
                        video = { post, shouldPlay, progress ->
                            component.videoPlayer()(
                                Modifier,
                                VideoPlayer.Spec(
                                    post.media,
                                    post.aspectRatio,
                                    progress,
                                    length,
                                    shouldPlay,
                                )
                            )
                            LaunchedEffect(Unit) {
                                if (!post.isViewed) {
                                    viewModel.view(post.id)
                                }
                            }
                        }
                    ) { post, path ->
                        component.imageView()(
                            Modifier,
                            ImageView.Spec(
                                path,
                                null,
                                ContentScale.Crop,
                                500f,
                            )
                        )
                        component.imageView()(
                            Modifier,
                            ImageView.Spec(path, post.aspectRatio, zoomable = true)
                        )
                        LaunchedEffect(Unit) {
                            if (!post.isViewed) {
                                viewModel.view(post.id)
                            }
                        }
                    }
                }
            }
        }
    }
}
