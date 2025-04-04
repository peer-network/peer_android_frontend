package eu.peernetwork.blog.ui.post.video

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
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
    val context = LocalContext.current
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var videoDuration by remember { mutableStateOf("--:--") }

    LaunchedEffect(video.media) {
        isLoading = true
        hasError = false
        thumbnailBitmap = withContext(Dispatchers.IO) {
            try {
                getVideoThumbnail(context, video.media.toUri())
            } catch (e: Exception) {
                hasError = true
                null
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(video.media) {
        withContext(Dispatchers.IO) {
            try {
                videoDuration = getVideoDuration(context, video.media.toUri())
            } catch (e: Exception) {
                videoDuration = "--:--"
            }
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            }
            thumbnailBitmap != null -> {
                Image(
                    bitmap = thumbnailBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Error loading thumbnail",
                        tint = Color.White
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Transparent)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = videoDuration,
                color = Color.White,
                fontSize = 12.sp
            )
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            VideoContent(
                username = remember { mutableStateOf(video.author.username) },
                userId = remember { mutableStateOf(video.author.slug.toString()) },
                timeStamp = remember { mutableStateOf(video.createdAt.formatTimeAgo(currentTime)) },
                descriptionText = remember { mutableStateOf(video.description) },
                isFullscreen = remember { mutableStateOf(true) },
                video = video.media.toUri(),
                avatar = {
                    AsyncImage(
                        model = video.author.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    val retriever = MediaMetadataRetriever()
    return try {
        try {
            retriever.setDataSource(context, uri)
        } catch (e: IllegalArgumentException) {
            retriever.setDataSource(uri.toString())
        }
        retriever.frameAtTime ?: retriever.getFrameAtTime(1000000)
    } catch (e: Exception) {
        null
    } finally {
        retriever.release()
    }
}

private fun getVideoDuration(context: Context, uri: Uri): String {
    val retriever = MediaMetadataRetriever()
    return try {
        try {
            retriever.setDataSource(context, uri)
        } catch (e: IllegalArgumentException) {
            retriever.setDataSource(uri.toString())
        }
        val durationMs = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )?.toLongOrNull() ?: 0L
        formatDuration(durationMs)
    } catch (e: Exception) {
        "--:--"
    } finally {
        retriever.release()
    }
}

private fun formatDuration(durationMs: Long): String {
    val seconds = (durationMs / 1000) % 60
    val minutes = (durationMs / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}