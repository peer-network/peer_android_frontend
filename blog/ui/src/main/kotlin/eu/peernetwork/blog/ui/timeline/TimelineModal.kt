package eu.peernetwork.blog.ui.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.ui.gallery.GalleryScreen
import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun TimelineModal(
    id: String,
    username: String,
    imageUrl: String,
    selected: MutableIntState,
    limit: Int,
    category: Category,
    criteria: Filter.Criteria,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    TimelineFullScreen(
        username = username,
        imageUrl = imageUrl,
        limit = limit,
        selected = selected,
        category = category,
        criteria = criteria,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component, post, index, pagerState ->
        GalleryScreen(
            position = index,
            current = selected,
            enabled = !pagerState.isScrollInProgress,
            post = post
        ) {
            if (id != post.author.id) {
                component.postUserFollow()(
                    modifier = Modifier,
                    PostUserConnection.Spec(
                        id = post.author.id,
                        isFollowing = post.author.following,
                        isFollowed = post.author.followed
                    )
                )
            }
        }
    }
}
