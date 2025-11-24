package eu.peernetwork.app.ui.profile.v2

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.ui.article.ArticleScreen
import eu.peernetwork.blog.ui.article.ArticleScreenEvent

@Composable
fun ProfilePosts(
    id: String,
    page: Int,
    limit: Int,
    timestamp: State<Long>,
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    postState: LazyListState = rememberLazyListState(),
    mediaState: LazyListState = rememberLazyListState(),
    onEvent: (ArticleScreenEvent) -> Unit,
) {
    val enable =  remember { mutableStateOf(false) }
    val requirePostUpdate =  remember { mutableStateOf(false) }
    ArticleScreen(
        author = id,
        types = if (page == 0) {
            PostUsecase.POST
        } else {
            PostUsecase.MEDIA
        },
        status = enable,
        postLimit = limit,
        requireUpdate = requirePostUpdate,
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
