package eu.peernetwork.blog.ui.interaction.overview.v2

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.interaction.listing.ListingScreen
import eu.peernetwork.blog.ui.interaction.overview.Overview
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignCollapsibleBottomSheet
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun OverviewScreen(
    state: MutableState<String?>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Overview.Builder::class.java).build(context)
    }
    val showSheet = remember { derivedStateOf { state.value != null } }
    val action = remember { mutableStateOf<(() -> Unit)?>(null) }
    val handleOnAuthorClick by rememberUpdatedState(onAuthorClick)
    DesignCollapsibleBottomSheet(
        onDismiss = {
            action.value?.invoke()
            action.value = null
            state.value = null },
        peekHeight = 400.dp,
        state = showSheet,
        content = {
            val content = remember { mutableStateOf(state.value) }
            OverviewScaffold(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize(),
                header = { pagerState, index ->

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
                    ListingScreen(
                        id = content.value ?: "",
                        postLimit = postLimit,
                        engagement = engagement,
                        provider = component,
                        onAuthorClick = {
                            action.value = { handleOnAuthorClick(it) }
                            state.value = null
                        },
                        viewModelStoreOwner = viewModelStoreOwner,
                        connection = connection
                    )
                }
            }
        }
    )
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
