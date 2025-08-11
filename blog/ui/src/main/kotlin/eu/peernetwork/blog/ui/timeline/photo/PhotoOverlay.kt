package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.PhotoPage
import eu.peernetwork.blog.ui.compose.PhotoPager
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView
import kotlinx.coroutines.flow.Flow

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
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is PhotoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as PhotoViewModel.State.Success).content
                )
                is PhotoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    val pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = {
        viewModel.load(Pageable(0, limit), category, criteria)
    })
    DragRefreshLayout(state = pullRefreshState) {
        DesignStatefulScaffold<Flow<PagingData<UiPost>>>(state = derivedState, onRefresh = {
            viewModel.load(Pageable(0, limit), category, criteria)
        }) { flow ->
            val lazyPagingItems = flow.collectAsLazyPagingItems()
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val refreshed = remember { derivedStateOf {
                    lazyPagingItems.loadState.refresh is LoadState.NotLoading
                } }
                EngagementScreen(
                    limit,
                    refreshed,
                    event::onMentionClick,
                    event::onHashtagClick,
                    event::onAuthorClick,
                    component,
                    viewModelStoreOwner
                ) { engagement ->
                    ModerationScreen(
                        component,
                        viewModelStoreOwner
                    ) { moderation ->
                        PhotoPage(
                            id = id,
                            position = position,
                            engagement = engagement,
                            moderation = moderation,
                            lazyPagingItems = lazyPagingItems,
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
                                    val media = post.media.first()
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
                            }
                        )
                    }
                }
            }
        }
    }
}
