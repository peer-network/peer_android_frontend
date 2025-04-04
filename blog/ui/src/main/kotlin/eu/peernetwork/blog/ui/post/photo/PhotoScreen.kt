package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
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
import eu.peernetwork.blog.ui.compose.PostScaffold
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun PhotoScreen(
    author: String,
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
    val data = viewModel.observe().collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            PhotoViewModel.State.Empty -> DesignStatefulContentState.Empty
            PhotoViewModel.State.Loading -> DesignStatefulContentState.Loading
            PhotoViewModel.State.Success -> {
                DesignStatefulContentState.Success(data.value)
            }
            is PhotoViewModel.State.Error -> {
                DesignStatefulContentState.Error((state as PhotoViewModel.State.Error).error)
            }
        }
    } }
    val page = remember { mutableStateOf(Pageable(0, postLimit)) }
    DesignStatefulContent<Flow<PagingData<UiPost>>>(
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
                items[index]?.let { photo ->
                    val slug = remember { derivedStateOf { photo.author.slug.toString() } }
                    val username = remember { derivedStateOf { photo.author.username } }
                    val imageUrl = remember { derivedStateOf { photo.author.imageUrl } }
                    val timestamp = remember { derivedStateOf {
                        photo.createdAt.formatTimeAgo(currentTime.longValue) }
                    }
                    PostScaffold(
                        username = username,
                        slug = slug,
                        imageUrl = imageUrl,
                        timeStamp = timestamp
                    ) { Box(modifier = Modifier.height(250.dp)) }
                }
            }
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
