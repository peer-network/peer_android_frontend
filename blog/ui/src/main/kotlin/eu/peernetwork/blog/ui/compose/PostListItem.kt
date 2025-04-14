package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.engagement.EngagementsComponent
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun LazyItemScope.PostListItem(
    post: UiPost,
    position: Int,
    state: State<Long>,
    onClick: (UiAction) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (UiPost) -> Unit
) {
    Spacer(modifier = Modifier.height(
        if (position == 0 && post.type == UiPost.Type.TEXT) {
            12.dp
        } else {
            0.dp
        }
    ))
    if (post.type != UiPost.Type.TEXT) {
        MediaPostCard(
            author = post.author,
            description = post.createdAt.formatTimeAgo(state.value),
            modifier = Modifier.padding(bottom = 18.dp),
            caption = {
                PostSummary(post.author.username, post.title, post.description)
            },
            engagements = {
                PostIcon(UiAction.Like, post.likes.toString(), onClick)
                PostIcon(UiAction.Dislike, post.dislikes.toString(), onClick)
                PostIcon(UiAction.Comment, post.comment.toString(), onClick)
            }
        ) { content(post) }
    } else {
        TextPostCard(
            author = post.author,
            description = post.createdAt.formatTimeAgo(state.value),
            modifier = Modifier.padding(bottom = 18.dp)
                .padding(horizontal = 8.dp),
            engagements = {
                PostIcon(UiAction.Like, post.likes.toString(), onClick)
                PostIcon(UiAction.Dislike, post.dislikes.toString(), onClick)
                PostIcon(UiAction.Comment, post.comment.toString(), onClick)
            }
        ) {
            PostText(
                post.title,
                post.description,
                Modifier.padding(top = 12.dp, bottom = 8.dp)
            )
        }
    }
}
