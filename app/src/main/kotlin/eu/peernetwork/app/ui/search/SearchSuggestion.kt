package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.explore.ExploreScreen
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.social.ui.search.member.MemberScreen
import eu.peernetwork.social.ui.search.tag.TagScreen
import eu.peernetwork.social.ui.search.title.TitleScreen

@Composable
fun SearchSuggestion(
    state: TextFieldState,
    mode: SearchMode,
    limit: Int,
    onClick: (String) -> Boolean,
    component: Search.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    listState: LazyGridState,
    modifier: Modifier = Modifier
) {
    val handleClick by rememberUpdatedState(onClick)
    when(mode) {
        is SearchMode.Username -> {
            MemberScreen(
                query = state,
                postLimit = limit,
                onClick = { handleClick(it.id) },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                modifier
            )
        }
        is SearchMode.Tag -> {
            TagScreen(
                query = state,
                postLimit = limit,
                onClick = { handleClick(it) },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                modifier
            )
        }
        is SearchMode.Title -> {
            TitleScreen(
                query = state,
                postLimit = limit,
                onClick = { handleClick(it.title) },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                modifier
            )
        }
        is SearchMode.Default -> {
            DesignScaffold(
                alwaysReturn = true,
                header = { Spacer(modifier = Modifier.height(64.dp)) },
                footer = {  },
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                ExploreScreen(
                    limit = limit,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    listState = listState
                ) { post, index -> }
            }
        }
    }
}
