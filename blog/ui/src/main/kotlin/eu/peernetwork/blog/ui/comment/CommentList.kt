package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun CommentList(
    limit: Int,
    username: String,
    imageUrl: String,
    state: MutableState<UiPostDetail?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val textField = remember { TextFieldState() }
    CommentScreen(
        username = username,
        imageUrl = imageUrl,
        limit = limit,
        post = state,
        comment = textField,
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
                    onLike = { interactor.like(comment) },
                    onReply = {
                        textField.edit {
                            replace(0, length, "@${comment.author.username}")
                        }
                    }
                )
            }
        }
    }
}
