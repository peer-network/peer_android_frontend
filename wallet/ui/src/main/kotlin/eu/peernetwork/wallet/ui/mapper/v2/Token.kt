package eu.peernetwork.wallet.ui.mapper.v2

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import eu.peernetwork.wallet.domain.model.Token
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.v2.UiToken

fun UiToken.mapToDomain(): Token {
    return when (this) {
        UiToken.Posts -> Token.Post
        UiToken.Likes -> Token.Like
        UiToken.DisLikes -> Token.DisLike
        UiToken.Comments -> Token.Comment
    }
}

@Composable
fun UiToken.mapToTitle(): String {
    return stringResource(
        when (this) {
            UiToken.Posts -> R.string.post_caption
            UiToken.Likes -> R.string.like_caption
            UiToken.DisLikes -> R.string.dislike_caption
            UiToken.Comments -> R.string.comment_caption
        }
    )
}

@Composable
fun UiToken.mapToFreeTitle(): String {
    return stringResource(
        when (this) {
            UiToken.Posts -> R.string.no_post_caption
            UiToken.Likes -> R.string.no_like_caption
            UiToken.DisLikes -> R.string.dislike_caption
            UiToken.Comments -> R.string.comment_caption
        }
    )
}

@Composable
fun UiToken.mapToIcon(): Painter {
    return painterResource(
        when (this) {
            UiToken.Posts -> R.drawable.ic_add
            UiToken.Likes -> R.drawable.ic_love
            UiToken.DisLikes -> R.drawable.ic_hate
            UiToken.Comments -> R.drawable.ic_comment
        }
    )
}

@Composable
fun UiToken.mapToDisclaimer(): String {
    return stringResource(
        when (this) {
            UiToken.Posts -> R.string.post_price_label
            UiToken.Likes -> R.string.like_price_label
            UiToken.DisLikes -> R.string.dislike_price_label
            UiToken.Comments -> R.string.comment_price_label
        }
    )
}
