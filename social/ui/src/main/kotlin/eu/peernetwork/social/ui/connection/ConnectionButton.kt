package eu.peernetwork.social.ui.connection

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.social.ui.R

@Composable
fun ConnectionButton(
    isFollowing: Boolean,
    isFollowed: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = 64.dp,
    minHeight: Dp = 42.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    val clickHandler by rememberUpdatedState {
        onClick(isFollowing)
    }
    val status = Pair(isFollowing, isFollowed).status()
    val style =  MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
    Crossfade(status) { target ->
        when(target) {
            ConnectionStatus.PEER -> {
                DesignButton(
                    style = style,
                    minWidth = minWidth,
                    minHeight = minHeight,
                    onClick = clickHandler,
                    contentPadding = contentPadding,
                    modifier = modifier,
                ) { Text(stringResource(R.string.peer_label)) }
            }
            ConnectionStatus.FOLLOWING -> {
                DesignButton(
                    style = style,
                    minWidth = minWidth,
                    minHeight = minHeight,
                    onClick = clickHandler,
                    modifier = modifier,
                    contentPadding = contentPadding,
                    colors = designSecondaryButtonColors(),
                ) { Text(stringResource(R.string.following_label)) }
            }
            ConnectionStatus.FOLLOWER -> {
                DesignOutlineButton(
                    style = style,
                    minWidth = minWidth,
                    minHeight = minHeight,
                    onClick = clickHandler,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    contentPadding = contentPadding,
                    modifier = modifier,
                ) { Text(stringResource(R.string.follow_label)) }
            }
        }
    }
}
