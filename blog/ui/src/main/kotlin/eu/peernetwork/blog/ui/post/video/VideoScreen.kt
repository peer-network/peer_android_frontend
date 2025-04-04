package eu.peernetwork.blog.ui.post.video

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import androidx.core.net.toUri
import androidx.paging.compose.LazyPagingItems

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

    // State for view mode and selected video
    val showDetailView = remember { mutableStateOf(false) }
    val selectedVideo = remember { mutableStateOf<UiVideo?>(null) }
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }

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

        if (showDetailView.value && selectedVideo.value != null) {
            // Detail view for selected video
            VideoDetailView(
                video = selectedVideo.value!!,
                currentTime = currentTime.longValue,
                onBack = { showDetailView.value = false }
            )
        } else {
            // Grid view of all videos
            VideoGridView(
                items = items,
                currentTime = currentTime.longValue,
                onVideoSelected = { video ->
                    selectedVideo.value = video
                    showDetailView.value = true
                }
            )
        }
    }
}

@Composable
private fun VideoGridView(
    items: LazyPagingItems<UiVideo>,
    currentTime: Long,
    onVideoSelected: (UiVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items.itemCount) { index ->
            items[index]?.let { video ->
                VideoGridItem(
                    video = video,
                    currentTime = currentTime,
                    onClick = { onVideoSelected(video) }
                )
            }
        }
    }
}

@Composable
private fun VideoGridItem(
    video: UiVideo,
    currentTime: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        // Thumbnail
        AsyncImage(
            model = video.media.toUri(), // Ensure this is the thumbnail URL
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Timestamp overlay
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = video.createdAt.formatTimeAgo(currentTime),
                color = Color.White,
                fontSize = 10.sp
            )
        }

        // Play icon overlay
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(36.dp)
                .background(Color.Black.copy(alpha = 0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                }
    }
}

@Composable
private fun VideoDetailView(
    video: UiVideo,
    currentTime: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        // Your existing video content
        val username = remember { mutableStateOf(video.author.username) }
        val userId = remember { mutableStateOf(video.author.slug.toString()) }
        val timeStamp = remember { mutableStateOf(video.createdAt.formatTimeAgo(currentTime)) }
        val descriptionText = remember { mutableStateOf(video.description) }
        val isFullscreen = remember { mutableStateOf(false) }

        VideoContent(
            username = username,
            userId = userId,
            timeStamp = timeStamp,
            descriptionText = descriptionText,
            isFullscreen = isFullscreen,
            video = video.media.toUri(),
            avatar = {
                AsyncImage(
                    model = video.author.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}