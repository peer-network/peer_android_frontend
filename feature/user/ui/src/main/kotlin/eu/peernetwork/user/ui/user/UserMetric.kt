package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.feature.user.ui.R
import eu.peernetwork.user.ui.model.UiMetric

enum class UserMetric {
    PUBLICATION,
    FOLLOWER,
    FOLLOWING,
    PEER
}

@Composable
fun UserMetric(
    overview: UiMetric,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    labelColor: Color = MaterialTheme.colorScheme.tertiary,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    onClick: (UserMetric) -> Unit
) {
    val handleOnClick by rememberUpdatedState(onClick)
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        UserMetric(
            title = { Text(overview.posts.toString()) },
            subTitle = { Text(stringResource(R.string.posts_label).lowercase()) },
            color = color,
            labelColor = labelColor,
            horizontalAlignment = Alignment.Start
        ) { handleOnClick(UserMetric.PUBLICATION) }
        UserMetric(
            title = { Text(overview.followers.toString()) },
            subTitle = { Text(stringResource(R.string.follower_label).lowercase()) },
            color = color,
            labelColor = labelColor,
            horizontalAlignment = Alignment.Start
        ) { handleOnClick(UserMetric.FOLLOWER) }
        UserMetric(
            title = { Text(overview.followed.toString()) },
            subTitle = { Text(stringResource(R.string.following_label).lowercase()) },
            color = color,
            labelColor = labelColor,
            horizontalAlignment = Alignment.Start
        ) { handleOnClick(UserMetric.FOLLOWING) }
        UserMetric(
            title = { Text(overview.peers.toString()) },
            subTitle = { Text(stringResource(R.string.peers_label).lowercase()) },
            color = color,
            labelColor = labelColor,
            horizontalAlignment = Alignment.Start
        ) { handleOnClick(UserMetric.PEER) }
    }
}

@Composable
fun UserMetric(
    title: @Composable () -> Unit,
    subTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    labelColor: Color = MaterialTheme.colorScheme.tertiary,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.SemiBold,
        color = color,
    ),
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium.copy(color = labelColor),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onClick: () -> Unit
) {
    val source = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = source,
            onClick = onClick
        ),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        CompositionLocalProvider(LocalTextStyle provides style) { title() }
        CompositionLocalProvider(LocalTextStyle provides labelStyle) { subTitle() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewMetrics() {
    PeerTheme {
        UserMetric(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    vertical = 16.dp,
                    horizontal = 24.dp
                ),
            overview = UiMetric(
                posts = 0,
                peers = 0,
                followed = 0,
                followers = 0
            )
        ) {}
    }
}
