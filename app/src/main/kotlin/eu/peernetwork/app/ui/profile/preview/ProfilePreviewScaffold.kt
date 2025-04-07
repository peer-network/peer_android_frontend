package eu.peernetwork.app.ui.profile.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ProfilePreviewScaffold(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    header: @Composable (State<Float>) -> Unit,
    content: @Composable () -> Unit
) {
    DesignScaffold(
        modifier = modifier.fillMaxSize(),
        header = header
    ) {
        DesignTab(pagerState) { index ->
            ProfileMedia.ROUTES[index].let {
                Icon(
                    painter = painterResource(id = it.id),
                    contentDescription = it.label?.let { stringResource(it) },
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(vertical = 8.dp).size(28.dp)
                )
            }
        }
        content()
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewProfilePreviewScaffold() {
    PeerTheme {
        ProfilePreviewScaffold(
            pagerState = rememberPagerState(pageCount = {0}),
            modifier = Modifier.fillMaxSize(),
            header = { Text("Header") },
            content = { Text("Content") },
        )
    }
}
