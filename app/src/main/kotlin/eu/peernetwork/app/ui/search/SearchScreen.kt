package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.search.member.MemberScreen
import eu.peernetwork.social.ui.search.tag.TagScreen
import eu.peernetwork.social.ui.search.title.TitleScreen

@Composable
fun SearchScreen(
    id: String,
    title: MutableState<DesignToolbarTitle>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Search.Builder::class.java).build(context)
    }
    SearchNavigation(
        id,
        title,
        component,
        viewModelStoreOwner
    ) { controller ->
        SearchScreen { mode, query ->
            if (mode == SearchMode.USERNAME) {
                MemberScreen(query, postLimit, {
                    controller.navigateIfNecessary("profile/$it")
                }, component, viewModelStoreOwner)
            } else if (mode == SearchMode.TAG) {
                TagScreen(query, postLimit, {
                    TODO("navigate to feed screen with tag filter")
                }, component, viewModelStoreOwner)
            } else if (mode == SearchMode.TITLE) {
                TitleScreen(query, postLimit, { id, type ->
                    controller.navigateIfNecessary("photo/$id")
                }, component, viewModelStoreOwner)
            } else {
                Box(modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState()))
            }
        }
    }
    LaunchedEffect(Unit) {
        title.value = DesignToolbarTitle(R.string.search_label)
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    content: @Composable (SearchMode?, TextFieldState) -> Unit
) {
    val query = remember { TextFieldState() }
    val mode = remember { mutableStateOf<SearchMode?>(null) }
    Column(modifier = modifier.fillMaxSize()) {
        SearchHeader(
            query,
            mode,
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top = 16.dp)
        )
        content(mode.value, query)
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    PeerTheme {
        SearchScreen { mode, query -> }
    }
}
