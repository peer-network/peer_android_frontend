package eu.peernetwork.social.ui.search.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SearchScreen(
    title: MutableState<DesignToolbarTitle>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    SearchScreen(
        onRefresh = {}
    )
    LaunchedEffect(Unit) {
        title.value = DesignToolbarTitle(R.string.search_label)
    }
}

@Composable
fun SearchScreen(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = remember { mutableStateOf(DesignStatefulScaffoldState.Success(Unit)) }
    DesignScaffold(
        modifier = modifier.fillMaxSize(),
        alwaysReturn = true,
        header = { SearchHeader() },
    ) { contentState ->
        DesignRefreshableScaffold<Unit>(
            state = state,
            modifier = Modifier.fillMaxSize(),
            onRefresh = onRefresh
        ) {

        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    PeerTheme {
        SearchScreen(
            onRefresh = {}
        )
    }
}
