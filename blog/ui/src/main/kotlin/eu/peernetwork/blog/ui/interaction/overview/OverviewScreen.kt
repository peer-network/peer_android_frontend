package eu.peernetwork.blog.ui.interaction.overview

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
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.interaction.listing.ListingScreen
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignCollapsibleBottomSheet
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlin.toString

@Composable
fun OverviewScreen(
    state: MutableState<UiContent?>,
    postLimit: Int,
    provider: UiComponentProvider,
    onAuthorClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Overview.Builder::class.java).build(context)
    }
    val viewModelStore = remember { UiViewModelStore.Delegate() }
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
                label = {
                    when(it) {
                        is UiAction.Like -> content.value?.likes
                        is UiAction.Dislike -> content.value?.dislikes
                        is UiAction.View -> content.value?.views
                        else -> 0
                    }.toString()
                }
            ) { pageState ->
                HorizontalPager(
                    state = pageState,
                    verticalAlignment = Alignment.Top,
                ) { page ->
                    val engagement = remember { when(page) {
                        1 -> Engagement.Content.Dislike
                        2 -> Engagement.Content.View
                        else -> Engagement.Content.Like
                    } }
                    val size = remember { when(page) {
                        1 -> content.value?.dislikes
                        2 -> content.value?.views
                        else -> content.value?.likes
                    } } ?: postLimit
                    val tag = remember { "${engagement::class.java.name}/${content.value?.id}/$size" }
                    ListingScreen(
                        id = content.value?.id ?: "",
                        postLimit = postLimit,
                        engagement = engagement,
                        provider = component,
                        onAuthorClick = {
                            action.value = { handleOnAuthorClick(it) }
                            state.value = null
                        },
                        viewModelStoreOwner = viewModelStore.get(tag),
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
            label = { "1" }
        ) {}
    }
}
