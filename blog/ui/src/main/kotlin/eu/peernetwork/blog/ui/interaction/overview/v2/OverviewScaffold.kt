package eu.peernetwork.blog.ui.interaction.overview.v2

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.design.material.DesignTabLayout
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun OverviewScaffold(
    modifier: Modifier = Modifier,
    header: @Composable (PagerState, Int) -> Unit,
    content: @Composable (PagerState) -> Unit
) {
    val tabs = remember { persistentListOf(
        UiAction.Like,
        UiAction.Dislike,
        UiAction.View
    ) }
    val state = rememberPagerState { tabs.size }
    val handleHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    Column(modifier) {
        DesignTabLayout(
            state = state,
            fitEvenly = false,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp),
            indicator = {
                Box(modifier = Modifier.fillMaxWidth()
                    .height(1.dp)
                    .background(PeerAppRed, shape = CircleShape))
            }
        ) {
            handleHeader(state, it)
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
            header = { pagerState, index -> }
        ) {}
    }
}
