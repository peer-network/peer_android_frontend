package eu.peernetwork.wallet.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.peernetwork.wallet.domain.model.Token
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiToken
import java.math.BigDecimal
import java.math.RoundingMode

fun UiToken.mapToDomain(): Token {
    return when(this) {
        UiToken.Like -> Token.Like
        UiToken.DisLike -> Token.DisLike
        UiToken.Comment -> Token.Comment
        UiToken.Post -> Token.Post
    }
}

@Composable
fun UiToken.title(): String {
    return when(this) {
        UiToken.Post -> stringResource(R.string.post_caption)
        UiToken.Like -> stringResource(R.string.like_caption)
        UiToken.Comment -> stringResource(R.string.comment_caption)
        UiToken.DisLike -> stringResource(R.string.dislike_caption)
    }
}

@Composable
fun UiToken.summary(price: BigDecimal, balance: BigDecimal): String {
    return when(this) {
        UiToken.Post -> stringResource(
            R.string.post_summary,
            "${price.setScale(2, RoundingMode.HALF_UP)}",
            "${balance.setScale(2, RoundingMode.HALF_UP)}"
        )
        UiToken.Like -> stringResource(
            R.string.like_summary,
            "${price.setScale(2, RoundingMode.HALF_UP)}",
            "${balance.setScale(2, RoundingMode.HALF_UP)}"
        )
        UiToken.Comment -> stringResource(
            R.string.comment_summary,
            "${price.setScale(2, RoundingMode.HALF_UP)}",
            "${balance.setScale(2, RoundingMode.HALF_UP)}"
        )
        UiToken.DisLike -> stringResource(
            R.string.dislike_summary,
            "${price.setScale(2, RoundingMode.HALF_UP)}",
            "${balance.setScale(2, RoundingMode.HALF_UP)}"
        )
    }
}
