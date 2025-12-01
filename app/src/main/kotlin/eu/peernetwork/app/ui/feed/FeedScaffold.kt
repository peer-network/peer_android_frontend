package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.luna.DesignTab

@Composable
fun FeedScaffold(
    pageState: PagerState,
    modifier: Modifier = Modifier,
    label: @Composable (Int) -> Unit,
    content: @Composable (Int) -> Unit
) {
    val updatedLabel by rememberUpdatedState(label)
    val updatedContent by rememberUpdatedState(content)
    Column(modifier = modifier.fillMaxSize()) {
        DesignTab(pageState) { index -> updatedLabel(index) }
        HorizontalPager(
            state = pageState,
            verticalAlignment = Alignment.Top,
        ) { page -> updatedContent(page) }
    }
}

@Composable
fun FeedScaffold(
    pageState: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    FeedScaffold(
        pageState = pageState,
        modifier = modifier,
        label = {
            Text(
                text = stringResource(id = if (it == 0) {
                    R.string.following_label
                } else {
                    R.string.followers_label
                } ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        },
        content = content
    )
}

@Composable
fun FeedExploreScaffold(
    pageState: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    FeedScaffold(
        pageState = pageState,
        modifier = modifier,
        label = {
            Text(
                text = stringResource(id = if (it == 0) {
                    R.string.latest_label
                } else {
                    R.string.trends_label
                }),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        },
        content = content
    )
}
