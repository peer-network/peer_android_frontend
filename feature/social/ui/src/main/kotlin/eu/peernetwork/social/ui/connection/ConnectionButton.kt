package eu.peernetwork.social.ui.connection

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
import eu.peernetwork.core.ui.design.luna.designPrimaryButtonColors
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.luna.designTertiaryButtonColors
import eu.peernetwork.feature.social.ui.R

@Composable
fun ConnectionButton(
    isFollowing: Boolean,
    isFollowed: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = 64.dp,
    minHeight: Dp = 42.dp,
    fontWeight: FontWeight = FontWeight.Bold,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    val clickHandler by rememberUpdatedState {
        onClick(isFollowing)
    }
    val status = Pair(isFollowing, isFollowed).status()
    val style =  MaterialTheme.typography.bodySmall.copy(fontWeight = fontWeight)
    DesignButton(
        style = style,
        minWidth = minWidth,
        minHeight = minHeight,
        onClick = clickHandler,
        contentPadding = contentPadding,
        modifier = modifier,
        colors = when(status) {
            ConnectionStatus.PEER -> designPrimaryButtonColors()
            ConnectionStatus.FOLLOWING -> designSecondaryButtonColors()
            ConnectionStatus.FOLLOWER -> designTertiaryButtonColors()
        }
    ) {
        when(status) {
            ConnectionStatus.PEER -> {
                Text(stringResource(R.string.peer_label))
            }
            ConnectionStatus.FOLLOWING -> {
                Text(stringResource(R.string.following_label))
            }
            ConnectionStatus.FOLLOWER -> {
                Text(stringResource(R.string.follow_label))
            }
        }
    }
}
