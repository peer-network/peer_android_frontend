package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.compose.ContentScaffold
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.PhotoPager
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun PhotoOverlay(
    id: String,
    limit: Int,
    position: Int,
    category: Category,
    criteria: Criteria? = null,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostEvent,
    header: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
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
    EngagementScreen(
        limit,
        event::onMentionClick,
        event::onHashtagClick,
        event::onAuthorClick,
        component,
        viewModelStoreOwner,
        connection
    ) { engagement ->
        ModerationScreen(
            component,
            viewModelStoreOwner
        ) { moderation ->
            ContentScaffold(
                state = derivedState,
                resource = component.resource(),
                onRefresh = {
                    viewModel.load(Pageable(0, limit), category, criteria)
                }
            ) { state, list ->
                if (list.value.loadState.refresh is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    PhotoPager(
                        id = id,
                        position = position,
                        engagement = engagement,
                        moderation = moderation,
                        lazyPagingItems = list,
                        onAuthorClick = event::onAuthorClick,
                        onMentionClick = event::onMentionClick,
                        onHashtagClick = event::onHashtagClick,
                        header = header,
                        connection = connection,
                        indicator = { state, items -> PhotoIndicator(state, items) },
                        content = { post, pagerState, active ->
                            if (post.media.size > 1) {
                                PhotoPager(
                                    pagerState,
                                    0f,
                                    post.media
                                ) { path ->
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
                                }
                            } else {
                                val media = remember { post.media.first() }
                                component.imageView()(
                                    Modifier,
                                    ImageView.Spec(
                                        media.path,
                                        null,
                                        ContentScale.Crop,
                                        500f,
                                    )
                                )
                                component.imageView()(
                                    Modifier,
                                    ImageView.Spec(media.path, null, zoomable = true)
                                )
                            }
                            LaunchedEffect(Unit) {
                                if (!post.isViewed) {
                                    viewModel.view(post.id)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
