package eu.peernetwork.app.ui.profile.v2

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.ui.article.ArticleEvent
import eu.peernetwork.blog.ui.article.ArticleList

@Composable
fun ProfilePosts(
    id: String,
    page: Int,
    limit: Int,
    selected: MutableIntState,
    timestamp: State<Long>,
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    postState: LazyListState = rememberLazyListState(),
    mediaState: LazyListState = rememberLazyListState(),
    onEvent: (ArticleEvent) -> Unit,
) {
    val enable =  remember { mutableStateOf(false) }
    ArticleList(
        author = id,
        types = if (page == 0) {
            PostUsecase.POST
        } else {
            PostUsecase.MEDIA
        },
        status = enable,
        limit = limit,
        selected = selected,
        timestamp = timestamp,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        onEvent = onEvent,
        listState =  if (page == 0) {
            postState
        } else {
            mediaState
        }
    )
}
