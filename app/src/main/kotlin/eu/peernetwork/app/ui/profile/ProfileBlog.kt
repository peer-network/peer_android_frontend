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
import eu.peernetwork.blog.domain.usecase.PhotosUsecase
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.feed.author.PostScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun ProfileBlog(
    id: String,
    enable: State<Boolean>,
    lastUpdated: State<Long>,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
    onNavigate: (Int) -> Unit = {},
    event: UiPostEvent,
    postState: LazyListState,
    mediaState: LazyListState,
) {
    ProfileBlog(onNavigate) { offset ->
        when (offset) {
            0 -> PostScreen(
                author = id,
                types = PhotosUsecase.POST,
                status = enable,
                postLimit = limit,
                lastUpdated = lastUpdated,
                provider = provider,
                viewModelStoreOwner = viewModelStore.get("$id${PhotosUsecase.POST}"),
                event = event,
                listState = postState
            )
            1 -> PostScreen(
                author = id,
                types = PhotosUsecase.MEDIA,
                status = enable,
                postLimit = limit,
                lastUpdated = lastUpdated,
                provider = provider,
                viewModelStoreOwner = viewModelStore.get("$id${PhotosUsecase.MEDIA}"),
                event = event,
                listState = mediaState
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
