package eu.peernetwork.blog.ui.timeline.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.compose.VideoPage
import eu.peernetwork.blog.ui.mapper.query
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.renderer.VideoPlayer
import kotlinx.coroutines.flow.Flow

@Composable
fun VideoOverlay(
    limit: Int,
    position: Int,
    enabled: Boolean,
    category: Category = Category.ALL,
    criteria: Criteria? = null,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onPostClick: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    header: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is VideoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as VideoViewModel.State.Success).data
                )
                is VideoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as VideoViewModel.State.Error).error
                )
            }
        }
    }
    val length = remember { mutableLongStateOf(0L) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = { viewModel.load(Pageable(0, limit), category, criteria) }
    )
    DragRefreshLayout(state = pullRefreshState) {
        DesignStatefulScaffold<Flow<PagingData<UiVideo>>>(
            state = derivedState,
            onRefresh = { viewModel.load(Pageable(0, limit), category, criteria) }
        ) { flow ->
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
                    postLimit = limit,
                    refresh = refreshed,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick,
                    onAuthorClick = onAuthorClick,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                ) { engagement ->
                    ModerationScreen(
                        component,
                        viewModelStoreOwner
                    ) { moderation ->
                        VideoPage(
                            position = position,
                            enabled = enabled,
                            engagement = engagement,
                            moderation = moderation,
                            lazyPagingItems = lazyPagingItems,
                            onLoad = { post ->
                                viewModel.videoBackground(
                                    "${post.media}${UiMimeType.Video.query()}",
                                    post.aspectRatio,
                                    configuration.screenWidthDp,
                                    configuration.screenHeightDp,
                                    true
                                )
                            },
                            onPostClick = onPostClick,
                            onAuthorClick = onAuthorClick,
                            onMentionClick = onMentionClick,
                            onHashtagClick = onHashtagClick,
                            progress = {
                                component.videoPlayer().Controller(
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                        .padding(end = 8.dp)
                                        .navigationBarsPadding(),
                                    progress = it,
                                    length = length
                                )
                            },
                            header = header,
                            connection = connection,
                            background = { DesignThumbnail(thumbnail.value[it]) },
                            content = { post, shouldPlay, progress ->
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
                            }
                        )
                    }
                }
            }
        }
    }
}
