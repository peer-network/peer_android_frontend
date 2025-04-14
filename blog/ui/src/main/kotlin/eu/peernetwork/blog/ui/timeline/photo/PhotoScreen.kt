package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.blog.ui.comment.CommentScreen
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.compose.PostListItem
import eu.peernetwork.blog.ui.engagement.EngagementsViewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.blog.ui.mapper.mapToProperty
import eu.peernetwork.media.core.renderer.ImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
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
    val engagementsViewModel = viewModel(
        modelClass = EngagementsViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulContentState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulContentState.Loading
                is PhotoViewModel.State.Success -> DesignStatefulContentState.Success(
                    (state as PhotoViewModel.State.Success).content
                )
                is PhotoViewModel.State.Error -> DesignStatefulContentState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    var isRefreshing by remember {
        mutableStateOf(derivedState.value is DesignStatefulContentState.Loading)
    }
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        viewModel.load(Pageable(0, postLimit))
    })
    DragRefreshLayout(
        state = pullRefreshState
    ) {
        DesignStatefulContent<Flow<PagingData<UiPost>>>(
            state = derivedState,
            refresh = { viewModel.load(Pageable(0, postLimit)) }
        ) { flow ->
            val lazyPagingItems = flow.collectAsLazyPagingItems()
            var showSheet = remember { mutableStateOf(false) }
            var selectedPostId = remember { mutableStateOf("") }
            DesignBottomSheet(
                showSheet = showSheet,
                tag = "designBottomSheet",
                onDismissRequest = { showSheet.value = false },
                color = Color.White.copy(alpha = 0.9f),
                sheetPeekHeight = 600.dp,
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            CommentScreen(
                                postId = selectedPostId,
                                postLimit = postLimit,
                                provider = component,
                                viewModelStoreOwner = viewModelStoreOwner,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }
                }
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index -> lazyPagingItems[index]?.id ?: index }
                ) { index ->
                    lazyPagingItems[index]?.let { post ->
                        PostListItem(
                            post = post,
                            position = index,
                            state = currentTime,
                            onClick = {},
                            provider = component,
                            viewModelStoreOwner = viewModelStoreOwner,
                            content = {
                                val media = post.media.first()
                                component.imageView()(
                                    Modifier,
                                    ImageView.Spec(media.path, media.mapToProperty())
                                )
                            }
                        )
                    }
                }
                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }
                item { Spacer(modifier = Modifier.height(56.dp)) }
            }
        }
    }
}
