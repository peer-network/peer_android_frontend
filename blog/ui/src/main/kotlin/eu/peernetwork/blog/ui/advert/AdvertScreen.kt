package eu.peernetwork.blog.ui.advert

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.usecase.PostUsecase.Companion.FEED
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.blog.ui.post.PostItem
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.flow.Flow

@Composable
fun AdvertScreen(
    id: String,
    postLimit: Int,
    listState: LazyListState,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    content: @Composable (
        Post.Handle,
        state: State<DesignStreamState<Flow<PagingData<UiPost>>>>,
    ) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Advert.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = AdvertViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.states.collectAsStateWithLifecycle()
    val updatedContent by rememberUpdatedState(content)
    val localState = remember { derivedStateOf {
        state[FEED.hashCode()] ?: AdvertViewModel.State.Empty
    } }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            val currentState = localState.value
            when (currentState) {
                AdvertViewModel.State.Empty -> DesignStreamState.Default
                AdvertViewModel.State.Loading -> DesignStreamState.Loading
                is AdvertViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is AdvertViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    PostScreen(
        id = id,
        limit = postLimit,
        listState = listState,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { handler -> updatedContent(handler, derivedState) }
    LaunchedEffect(Unit) {
        viewModel(FEED, Pageable(0, postLimit))
    }
}

fun LazyListScope.advert(
    state: LazyPagingItems<UiPost>,
    onMenu: () -> Unit,
    engagement: @Composable (UiPost) -> Unit,
    connection: @Composable RowScope.(UiPost) -> Unit,
    content: @Composable (UiPost, String, Int) -> Unit,
) {
    items(
        count = state.itemCount,
        key = { index -> state[index]?.id?.let { "$it;$index" } ?: index }
    ) { index ->
        val updatedEngagement by rememberUpdatedState(engagement)
        val updatedConnection by rememberUpdatedState(connection)
        val updatedContent by rememberUpdatedState(content)
        state[index]?.let { post ->
            PostItem(
                type = post.type,
                pinnedBy = null,
                model = post.mapToDetail(),
                media = post.media,
                onMenu = onMenu,
                onClick = onMenu,
                engagement = { updatedEngagement(post) },
                connection = { updatedConnection(post) },
                content = { path -> updatedContent(post, path, index) }
            )
        }
    }
}
