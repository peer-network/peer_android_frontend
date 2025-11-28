package eu.peernetwork.blog.ui.interaction.overview

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.engagement.EngagementMetric
import eu.peernetwork.blog.ui.interaction.user.UserList
import eu.peernetwork.blog.ui.model.v2.UiEngagement
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch

@Composable
fun OverviewScreen(
    state: MutableState<UiEngagement?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val component = remember {
        provider.builder(Overview.Builder::class.java).build(context)
    }
    OverviewSheet(state) {
        val icons = listOf(
            R.drawable.ic_love_outline to it.likes,
            R.drawable.ic_hate_outline to it.dislikes,
            R.drawable.ic_view to it.views
        )
        OverviewScaffold(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize(),
            header = { pagerState, index ->
                EngagementMetric(
                    text = icons[index].second,
                    painter = painterResource(icons[index].first),
                    orientation = Orientation.Horizontal,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) { scope.launch { pagerState.scrollToPage(index) } }
            }
        ) { pageState ->
            HorizontalPager(
                state = pageState,
                verticalAlignment = Alignment.Top,
            ) { page ->
                val engagement = remember {
                    when (page) {
                        1 -> Engagement.Content.Dislike
                        2 -> Engagement.Content.View
                        else -> Engagement.Content.Like
                    }
                }
                UserList(
                    id = it.id,
                    limit = postLimit,
                    engagement = engagement,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewCreatorScreen() {
    PeerTheme {
        OverviewScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            header = { pagerState, index -> }
        ) {}
    }
}
