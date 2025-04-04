package eu.peernetwork.blog.ui.post.video

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import eu.peernetwork.blog.ui.compose.PostScaffold
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.view.DesignStatefulContent
import eu.peernetwork.core.ui.design.view.DesignStatefulContentState
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun VideoScreen(
    author: String,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val data = viewModel.observe().collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            VideoViewModel.State.Empty -> DesignStatefulContentState.Empty
            VideoViewModel.State.Loading -> DesignStatefulContentState.Loading
            VideoViewModel.State.Success -> {
                DesignStatefulContentState.Success(data.value)
            }
            is VideoViewModel.State.Error -> {
                DesignStatefulContentState.Error((state as VideoViewModel.State.Error).error)
            }
        }
    } }
    val page = remember { mutableStateOf(Pageable(0, postLimit)) }

    DesignStatefulContent<Flow<PagingData<UiVideo>>>(
        state = derivedState,
        refresh = { viewModel.load(author, page.value) }
    ) {
        val items = it.collectAsLazyPagingItems()
        val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(60_000L)
                currentTime.longValue = System.currentTimeMillis()
            }
        }
        LazyColumn {
            items(
                count = items.itemCount,
                key = { index -> items[index]?.id ?: index }
            ) { index ->
                items[index]?.let { video ->
                    val username = remember { mutableStateOf(video.author.username) }
                    val userId = remember { mutableStateOf(video.author.slug.toString()) }
                    val timeStamp = remember {
                        mutableStateOf(video.createdAt.formatTimeAgo(currentTime.longValue))
                    }
                    val descriptionText = remember { mutableStateOf(video.description) }
                    val isFullscreen = remember { mutableStateOf(false) }

                    // Avatar composable
                    val avatar: @Composable () -> Unit = {
                        AsyncImage(
                            model = video.author.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    // Use VideoContent with all required parameters
                    VideoContent(
                        username = username,
                        userId = userId,
                        timeStamp = timeStamp,
                        descriptionText = descriptionText,
                        isFullscreen = isFullscreen,
                        video = Uri.parse(video.media), // Convert String URL to Uri
                        avatar = avatar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp) // Adjust height as needed
                    )
                }
            }
        }
    }
}
