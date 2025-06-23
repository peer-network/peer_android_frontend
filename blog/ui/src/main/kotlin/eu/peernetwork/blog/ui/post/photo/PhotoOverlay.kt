package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView
import kotlinx.coroutines.flow.Flow

@Composable
fun PhotoOverlay(
    author: String,
    limit: Int,
    position: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
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
                is PhotoViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as PhotoViewModel.State.Success).content
                    )
                }
                is PhotoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    val pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = {
        viewModel.load(author, Pageable(0, limit))
    })
    DragRefreshLayout(state = pullRefreshState) {
        DesignStatefulScaffold<Flow<PagingData<UiPost>>>(state = derivedState, onRefresh = {
            viewModel.load(author, Pageable(0, limit))
        }) { flow ->
            val lazyPagingItems = flow.collectAsLazyPagingItems()
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val pagerState = rememberPagerState(
                    initialPage = position
                ) { lazyPagingItems.itemCount }
                VerticalPager(pagerState) { page ->
                    val post = lazyPagingItems[page]
                    if (post != null) {
                        if (post.media.size > 1) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                val pagerState =
                                    rememberPagerState(initialPage = 0) { post.media.size }
                                HorizontalPager(state = pagerState) {
                                    val media = post.media[it]
                                    component.imageView()(
                                        Modifier,
                                        ImageView.Spec(media.path, post.aspectRatio)
                                    )
                                }
                            }
                        } else {
                            val media = post.media.first()
                            component.imageView()(
                                Modifier,
                                ImageView.Spec(media.path, post.aspectRatio)
                            )
                        }
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
