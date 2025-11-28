package eu.peernetwork.blog.ui.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun CommentList(
    limit: Int,
    state: MutableState<UiPostDetail?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    CommentScreen(
        limit = limit,
        post = state,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, interactor, items ->
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            items[index]?.let { comment ->
                CommentItem(
                    slug = comment.author.slug.toString(),
                    username = comment.author.username,
                    imageUrl = comment.author.imageUrl,
                    comment = comment.content,
                    isLiked = interactor.observe().value[comment.id]?.isLiked ?: comment.isLiked,
                    likes = comment.likes,
                    onViewLikes = { interactor.viewLike(comment.id) },
                    onLike = { interactor.like(comment) }
                )
            }
        }
    }
}
