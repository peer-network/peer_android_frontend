package eu.peernetwork.app.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.connection.ConnectionScreen

@Composable
fun FeedScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Feed.Component, FeedViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = FeedViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { updatedContent(component, viewModel) }
}

@Composable
fun FeedScreen(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    refresh: MutableState<Boolean>,
    title: String? = null,
    criteria: Criteria? = null,
) {
    val controller = rememberNavController()
    val selected = remember { mutableIntStateOf(-1) }
    FeedScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner,
    ) { component, viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val ordinal = remember {
            derivedStateOf {
                (state as? FeedViewModel.State.Initialize?)?.filter ?: 0
            }
        }
        FeedNavigation(
            id = id,
            component = component,
            controller = controller,
        ) {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val position = remember { mutableIntStateOf(state.page) }
            FeedPage(
                id = id,
                title = title,
                selected = selected,
                limit = limit,
                ordinal = ordinal.value,
                criteria = criteria,
                position = position,
                refresh = refresh,
                component = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) { viewModel.setFilter(it) }
        }
    }
}
