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
    return stringResource(R.string.post_caption)
}

@Composable
fun UiIntent.summary(vararg formatArgs: Any): String {
    return stringResource(R.string.post_condition, *formatArgs)
}
