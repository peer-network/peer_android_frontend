package eu.peernetwork.user.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.model.UiOverview

@Composable
fun Overview(
    overview: UiOverview,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Overview(
            title = { Text("${overview.followers}") },
            subTitle = { Text(stringResource(R.string.follower_label)) },
            modifier = Modifier.padding(horizontal = 8.dp).clickable { onClick(0) },
            horizontalAlignment = Alignment.Start
        )
        Overview(
            title = { Text("${overview.followed}") },
            subTitle = { Text(stringResource(R.string.following_label)) },
            modifier = Modifier.padding(horizontal = 8.dp).clickable { onClick(1) },
            horizontalAlignment = Alignment.Start
        )
        Overview(
            title = { Text("${overview.peers}") },
            subTitle = { Text(stringResource(R.string.peers_label)) },
            modifier = Modifier.padding(horizontal = 8.dp).clickable { onClick(2) },
            horizontalAlignment = Alignment.Start
        )
        Overview(
            title = { Text("${overview.posts}") },
            subTitle = { Text(stringResource(R.string.post_label)) },
            modifier = Modifier.padding(horizontal = 8.dp).clickable { onClick(3) },
            horizontalAlignment = Alignment.Start
        )
    }
}

@Composable
fun Overview(
    title: @Composable () -> Unit,
    subTitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        CompositionLocalProvider(LocalTextStyle provides textStyle.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )) { title() }
        CompositionLocalProvider(LocalTextStyle provides textStyle.copy(
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )) { subTitle() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserOverview() {
    PeerTheme {
        Overview(
            modifier = Modifier.fillMaxWidth(),
            overview = UiOverview(
                posts = 0,
                peers = 0,
                followed = 0,
                followers = 0
            )
        ) {}
    }
}
