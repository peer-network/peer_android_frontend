package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.CommentSummary
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiComment

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CommentListing(
    likes: State<Map<String, UiComment>>,
    lazyPagingItems: LazyPagingItems<UiComment>,
    size: Size,
    onLike: (UiComment) -> Unit = {},
    onComment: (UiComment) -> Unit = {},
    titleOnClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
) {
    val haptic = LocalHapticFeedback.current
    val handleOnLike by rememberUpdatedState(onLike)
    val handleOnComment by rememberUpdatedState(onComment)
    val handleTitleOnClick by rememberUpdatedState(titleOnClick)
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { comment ->
                val interactionSource = remember { MutableInteractionSource() }
                CommentSummary(
                    model = comment.mapToContent(),
                    likes = likes.value[comment.id]?.likes ?: comment.likes,
                    modifier = Modifier.combinedClickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        onClick = { },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            handleOnComment(comment)
                        }
                    ).padding(horizontal = 24.dp)
                        .padding(top = 12.dp),
                    onComment = { handleOnComment(comment) },
                    titleOnClick = { handleTitleOnClick(comment.author.username) },
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick,
                    onAuthorClick = onAuthorClick
                ) {
                    CommentOptions(
                        likes = likes.value[comment.id]?.likes ?: comment.likes,
                        isLiked = likes.value[comment.id]?.isLiked ?: comment.isLiked,
                        onComment = { handleOnComment(comment) }
                    ) { handleOnLike(comment) }
                }
            }
        }
        item { Box(modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = size.height.dp)) }
    }
}
