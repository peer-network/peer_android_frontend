package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.compose.PostListItem
import eu.peernetwork.blog.ui.mapper.mapToProperty
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(
    author: String,
    postLimit: Int,
    loadState: MutableState<Boolean>,
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
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulContentState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulContentState.Loading
                is PhotoViewModel.State.Success -> {
                    DesignStatefulContentState.Success(
                        (state as PhotoViewModel.State.Success).content
                    )
                }
                is PhotoViewModel.State.Error -> DesignStatefulContentState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    DesignStatefulContent<Flow<PagingData<UiPost>>>(
        state = derivedState,
        refresh = { viewModel.load(author, Pageable(0, postLimit)) }
    ) { flow ->
        val lazyPagingItems = flow.collectAsLazyPagingItems()
        LazyColumn {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> index }
            ) { index ->
                lazyPagingItems[index]?.let { photo ->
                    PostListItem(
                        photo, index, currentTime, onClick = {},
                        provider = provider,
                        viewModelStoreOwner = viewModelStoreOwner
                    ) {
                        val media = photo.media.first()
                        component.imageView()(
                            Modifier,
                            ImageView.Spec(media.path, media.mapToProperty())
                        )
                    }
                }
            }
            if (lazyPagingItems.loadState.append is LoadState.Loading) {
                item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
            }
            item { Spacer(modifier = Modifier.height(56.dp)) }
        }
    }
    LaunchedEffect(derivedState.value) {
        loadState.value = derivedState.value is DesignStatefulContentState.Loading
    }
    LaunchedEffect(loadState.value) {
        if (loadState.value) {
            viewModel.load(author, Pageable(0, postLimit))
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
}

fun Long.formatTimeAgo(time: Long): String {
    val diff = time - this
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        else -> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = this
            String.format(Locale.getDefault(), "%1\$tb %1\$td, %1\$tY", calendar)
        }
    }
}
