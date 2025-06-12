package eu.peernetwork.wallet.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.peernetwork.wallet.domain.model.Intent
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiIntent

fun UiIntent.mapToDomain(): Intent {
    return when(this) {
        UiIntent.Like -> Intent.Like
        UiIntent.DisLike -> Intent.DisLike
        UiIntent.Comment -> Intent.Comment
        UiIntent.Post -> Intent.Post
    }
}

@Composable
fun UiIntent.title(): String {
    return when(this) {
        UiIntent.Post -> stringResource(R.string.post_caption)
        UiIntent.Like -> stringResource(R.string.like_caption)
        UiIntent.Comment -> stringResource(R.string.comment_caption)
        UiIntent.DisLike -> stringResource(R.string.dislike_caption)
    }
}

@Composable
fun UiIntent.summary(vararg formatArgs: Any): String {
    return when(this) {
        UiIntent.Post -> stringResource(R.string.post_summary, *formatArgs)
        UiIntent.Like -> stringResource(R.string.like_summary, *formatArgs)
        UiIntent.Comment -> stringResource(R.string.comment_summary, *formatArgs)
        UiIntent.DisLike -> stringResource(R.string.dislike_summary, *formatArgs)
    }
}
