package eu.peernetwork.blog.ui.comment

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignRichText

@Composable
fun CommentList(
    id: String,
    uuid: String,
    limit: Int,
    isAuthor: Boolean,
    controller: NavHostController,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onContentClick: (DesignRichText, String) -> Unit,
    onReport: (String) -> Unit,
    onReply: (String) -> Unit,
    onClick: (String) -> Unit
) {
    val handleReply by rememberUpdatedState(onReply)
    val handleClick by rememberUpdatedState(onClick)
    val handleReport by rememberUpdatedState(onReport)
    CommentScreen(
        id = id,
        uuid = uuid,
        limit = limit,
        controller = controller,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
        onUserClick = { handleClick(it) }
    ) { _, interactor, items ->
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
        ) { index ->
            items[index]?.let { comment ->
                CommentUserMask(
                    isAuthor = isAuthor,
                    status = comment.author.status,
                    isAccessible = comment.author.isAccessible,
                    onClick = { handleClick(comment.author.id) },
                    description = {
                        CommentMask(
                            status = comment.status,
                            isAccessible = comment.isAccessible,
                        ) {
                            CommentOption({ handleReport(comment.id) }) {
                                DesignRichText(
                                    text = comment.content,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 3,
                                    onClick = onContentClick
                                )
                            }
                        }
                    }
                ) {
                    CommentItem(
                        slug = comment.author.slug.toString(),
                        username = comment.author.username,
                        imageUrl = comment.author.imageUrl,
                        isLiked = interactor.observe().value[comment.id]?.isLiked ?: comment.isLiked,
                        isReported = comment.isReported,
                        likes = comment.likes,
                        onViewLikes = { interactor.viewLike(comment.id) },
                        onLike = { interactor.like(comment) },
                        onReply = {
                            handleReply(comment.author.username)
                        },
                        onClick = { handleClick(comment.author.id) }
                    ) {
                        CommentMask(
                            status = comment.status,
                            isAccessible = comment.isAccessible,
                        ) {
                            CommentOption({ handleReport(comment.id) }) {
                                DesignRichText(
                                    text = comment.content,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 3,
                                    onClick = onContentClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
