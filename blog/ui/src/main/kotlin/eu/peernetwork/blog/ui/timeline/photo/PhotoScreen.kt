package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.compose.PostListItem
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.blog.ui.mapper.mapToProperty
import eu.peernetwork.core.ui.design.component.DesignPagingContent
import eu.peernetwork.core.ui.design.component.DesignRefreshableContent
import eu.peernetwork.media.core.renderer.ImageView
import kotlinx.coroutines.delay

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
    val refreshEngagement = remember { mutableStateOf(false) }
    DesignPagingContent<UiPost>(
        state = derivedState,
        onRefresh = { viewModel.load(Pageable(0, postLimit)) },
        placeholder = { PostPageSkeleton() }
    ) { state, lazyPagingItems ->
        val refreshState = remember { derivedStateOf {
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                DesignStatefulContentState.Loading
            } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                DesignStatefulContentState.Error(
                    (lazyPagingItems.loadState.refresh as LoadState.Error).error
                )
            } else {
                state.value
            }
        } }
        DesignRefreshableContent<LazyPagingItems<UiPost>>(
            state = refreshState,
            onRefresh = {
                refreshEngagement.value = true
                lazyPagingItems.refresh() }
        ) {
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
                            engagements = {
                                EngagementScreen(
                                    post.mapToContent(),
                                    postLimit,
                                    refreshEngagement,
                                    component,
                                    viewModelStoreOwner
                                ) },
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
