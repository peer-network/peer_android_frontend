package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignTab
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.media.core.model.UiMimeType

@Composable
fun ProfileScaffold(
    pageState: PagerState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    header: @Composable (State<Float>) -> Unit,
    content: @Composable (Int) -> Unit
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    DesignRefreshScaffold(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        DesignScaffold(
            modifier = modifier.fillMaxSize(),
            header = updatedHeader,
        ) {
            DesignTab(pageState) { index ->
                UiMimeType.get(index)?.let {
                    Icon(
                        painter = painterResource(id = it.id),
                        contentDescription = it.label?.let { stringResource(it) },
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 6.dp)
                            .size(18.dp)
                    )
                }
            }
            HorizontalPager(
                state = pageState,
                verticalAlignment = Alignment.Top,
            ) { page -> updatedContent(page) }
        }
    }
}
