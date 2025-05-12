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

sealed interface SearchState {
    data object Default : SearchState
    sealed class Active(val value: String) : SearchState {
        data class Username(private val query: String) : Active(query)
        data class Tag(private val query: String): Active(query)
    }
}

@Composable
fun SearchScreen(
    id: String,
    title: MutableState<DesignToolbarTitle>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    searchState: SearchState = SearchState.Default,
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
        SearchScreen(state = searchState) { mode, query ->
            if (mode == SearchMode.USERNAME) {
                MemberScreen(query, postLimit, {
                    controller.navigateIfNecessary("profile/$it")
                }, component, viewModelStoreOwner)
            } else if (mode == SearchMode.TAG) {
                TagScreen(query, postLimit, {
                    controller.navigateIfNecessary("feed/$it")
                }, component, viewModelStoreOwner)
            } else if (mode == SearchMode.TITLE) {
                TitleScreen(query, postLimit, {
                    controller.navigateIfNecessary("search/${it.title}")
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
    state: SearchState? = null,
    content: @Composable (SearchMode?, TextFieldState) -> Unit
) {
    val query = remember(state) { TextFieldState((state as? SearchState.Active)?.value ?: "") }
    val mode = remember(state) {
        mutableStateOf(
            when (state) {
                is SearchState.Active.Username -> SearchMode.USERNAME
                is SearchState.Active.Tag -> SearchMode.TAG
                else -> null
            }
        )
    }

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
