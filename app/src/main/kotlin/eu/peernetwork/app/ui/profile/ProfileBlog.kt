package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.post.photo.PhotoScreen
import eu.peernetwork.blog.ui.post.video.VideoScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun ProfileBlog(
    id: String,
    enable: Boolean,
    lastUpdated: State<Long>,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onNavigate: (Int) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClicked: (String) -> Unit = {},
    onPhotoClick: (String, Int) -> Unit = { id, position -> },
    onVideoClick: (String, Int) -> Unit = { id, position -> },
    photoState: LazyListState,
    videoState: LazyListState
) {
    ProfileBlog(onNavigate) { offset ->
        when (offset) {
            0 -> PhotoScreen(
                author = id,
                postLimit = limit,
                lastUpdated = lastUpdated,
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner,
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick,
                onPostClick = onPhotoClick,
                onAuthorClick = onAuthorClicked,
                listState = photoState
            )
            1 -> VideoScreen(
                author = id,
                enable = enable,
                postLimit = limit,
                lastUpdated = lastUpdated,
                provider = provider,
                viewModelStoreOwner = viewModelStoreOwner,
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick,
                onAuthorClick = onAuthorClicked,
                onPostClick = onVideoClick,
                listState = videoState
            )
        }
    }
}

@Composable
private fun ProfileBlog(
    onNavigate: (Int) -> Unit = {},
    content: @Composable (Int) -> Unit
) {
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = 0
    )
    val handleNavigation by rememberUpdatedState(onNavigate)
    DesignTab(pageState) { index ->
        UiMimeType.get(index)?.let {
            Icon(
                painter = painterResource(id = it.id),
                contentDescription = it.label?.let { stringResource(it) },
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(vertical = 8.dp).size(28.dp)
            )
        }
    }
    HorizontalPager(
        state = pageState,
        verticalAlignment = Alignment.Top,
    ) { page -> content(page) }
    LaunchedEffect(pageState.currentPage) { handleNavigation(pageState.currentPage) }
}
