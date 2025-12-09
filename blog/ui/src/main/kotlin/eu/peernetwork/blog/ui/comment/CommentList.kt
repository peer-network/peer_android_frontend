package eu.peernetwork.blog.ui.comment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignRichText

@Composable
fun CommentList(
    id: String,
    limit: Int,
    controller: NavHostController,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onContentClick: (DesignRichText, String) -> Unit,
    onReply: (String) -> Unit,
    onClick: (String) -> Unit
) {
    val handleReply by rememberUpdatedState(onReply)
    val handleClick by rememberUpdatedState(onClick)
    CommentScreen(
        id = id,
        limit = limit,
        controller = controller,
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
                        handleReply(comment.author.username)
                    },
                    onContentClick = onContentClick
                ) { handleClick(comment.author.id) }
            }
        }
    }
}
