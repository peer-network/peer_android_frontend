package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun SearchScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Search.Component) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Search.Builder::class.java).build(context) }
    val updatedContent by rememberUpdatedState(content)
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { updatedContent(component) }
}

@Composable
fun SearchScreen(
    id: String,
    limit: Int,
    mode: SearchMode,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    title: String? = null,
    query: String? = null
) {
    val controller = rememberNavController()
    val currentMode = remember { mutableStateOf(mode) }
    val listState = rememberLazyGridState()
    SearchScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component ->
        SearchNavigation(
            userId = id,
            controller = controller,
            component = component
        ) {
            val state = remember { TextFieldState(query ?: "") }
            SearchPage(
                state,
                currentMode
            ) {
                SearchSuggestion(
                    state = state,
                    mode = currentMode.value,
                    limit = limit,
                    onClick = { false },
                    component = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    listState = listState,
                    modifier = Modifier.padding(top = 36.dp)
                )
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
}
