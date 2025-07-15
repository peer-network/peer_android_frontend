package eu.peernetwork.app.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.explore.ExploreScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
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
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    searchState: SearchState = SearchState.Default,
    title: String? = null
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Search.Builder::class.java).build(context) }
    val session = rememberSaveable { System.currentTimeMillis() }
    SearchNavigation(
        id,
        component,
        viewModelStore
    ) { controller ->
        SearchScreen(state = searchState) { mode, query ->
            if (mode == SearchMode.USERNAME) {
                MemberScreen(query, postLimit, {
                    controller.navigateIfNecessary("profile/${it.id}")
                    false
                }, component, viewModelStore.get("$session"),
                    Modifier.padding(top = 36.dp))
            } else if (mode == SearchMode.TAG) {
                TagScreen(query, postLimit, {
                    controller.navigateIfNecessary("feed/$it")
                }, component, viewModelStore.get("$session"),
                    Modifier.padding(top = 36.dp))
            } else if (mode == SearchMode.TITLE) {
                TitleScreen(query, postLimit, {
                    controller.navigateIfNecessary("search?query=${it.title}")
                }, component, viewModelStore.get("$session"),
                    Modifier.padding(top = 36.dp))
            } else {
                ExploreScreen(
                    postLimit = postLimit,
                    provider = component,
                    viewModelStoreOwner = viewModelStore.get("$session"),
                )
            }
        }
        DesignTitleBarHost("SearchScreen") {
            titleBar {
                DesignTitle {
                    Text(title ?: stringResource(R.string.search_label))
                }
            }
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState? = null,
    content: @Composable (SearchMode?, TextFieldState) -> Unit
) {
    val query = rememberSaveable(stateSaver = TextFieldState.Saver) {
        mutableStateOf(TextFieldState((state as? SearchState.Active)?.value ?: ""))
    }
    val mode = rememberSaveable(state) {
        mutableStateOf(
            when (state) {
                is SearchState.Active.Username -> SearchMode.USERNAME
                is SearchState.Active.Tag -> SearchMode.TAG
                else -> null
            }
        )
    }
    val updatedContent by rememberUpdatedState(content)
    Box(modifier = modifier.fillMaxSize()) {
        updatedContent(mode.value, query.value)
        SearchHeader(
            query.value,
            mode,
            modifier = Modifier.padding(horizontal = 24.dp)
                .padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    PeerTheme {
        SearchScreen { mode, query ->
            Box(modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant))
        }
    }
}
