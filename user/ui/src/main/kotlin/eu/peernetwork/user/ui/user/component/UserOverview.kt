package eu.peernetwork.user.ui.user.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignLegend
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.model.UiOverview

@Composable
fun UserOverview(
    modifier: Modifier = Modifier,
    overview: UiOverview,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DesignLegend(
            title = { Text("${overview.posts}") },
            subTitle = { Text(stringResource(eu.peernetwork.user.ui.R.string.post_label)) },
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.Start
        )
        DesignLegend(
            title = { Text("${overview.followers}") },
            subTitle = { Text(stringResource(eu.peernetwork.user.ui.R.string.follower_label)) },
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.Start
        )
        DesignLegend(
            title = { Text("${overview.followed}") },
            subTitle = { Text(stringResource(eu.peernetwork.user.ui.R.string.following_label)) },
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.Start
        )
        DesignLegend(
            title = { Text("${overview.peers}") },
            subTitle = { Text(stringResource(eu.peernetwork.user.ui.R.string.peers_label)) },
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.Start
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserOverview() {
    PeerTheme {
        UserOverview(
            modifier = Modifier.fillMaxWidth(),
            overview = UiOverview(
                posts = 0,
                peers = 0,
                followed = 0,
                followers = 0
            )
        )
    }
}
