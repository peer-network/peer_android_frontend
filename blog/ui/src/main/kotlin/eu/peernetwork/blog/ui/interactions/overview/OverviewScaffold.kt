package eu.peernetwork.blog.ui.interactions.overview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.design.compose.DesignTabLayout
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun OverviewScaffold(
    modifier: Modifier = Modifier,
    label: (UiAction) -> String,
    content: @Composable (PagerState) -> Unit
) {
    val tabs = remember { persistentListOf(
        UiAction.Like,
        UiAction.Dislike,
        UiAction.View
    ) }
    val scope = rememberCoroutineScope()
    val state = rememberPagerState { tabs.size }
    val updatedLabel by rememberUpdatedState(label)
    val updatedContent by rememberUpdatedState(content)
    Column(modifier) {
        DesignTabLayout(
            state = state,
            fitEvenly = false,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            PostIcon(
                action = tabs[it],
                value = updatedLabel(tabs[it]),
                padding = PaddingValues(end = 8.dp),
                color = if (state.currentPage == it) {
                    PeerAppRed
                } else {
                    MaterialTheme.colorScheme.tertiary
                },
            ) { action ->
                scope.launch { state.animateScrollToPage(tabs.indexOf(action)) }
            }
        }
        updatedContent(state)
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignTabLayout() {
    PeerTheme {
        OverviewScaffold(
            modifier = Modifier.fillMaxSize()
                .padding(top = 8.dp),
            label = { "1" }
        ) {}
    }
}
