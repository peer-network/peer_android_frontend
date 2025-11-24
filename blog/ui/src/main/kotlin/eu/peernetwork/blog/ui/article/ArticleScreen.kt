package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.extension.share
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

sealed interface ArticleScreenEvent {
    data class Post(val id: String): ArticleScreenEvent
    data class Boost(val id: String): ArticleScreenEvent
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ArticleScreen(
    author: String,
    types: Set<Content.Type>,
    postLimit: Int,
    status: State<Boolean>,
    requireUpdate: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onEvent: (ArticleScreenEvent) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember { provider.builder(Article.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = ArticleViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.states.collectAsStateWithLifecycle()
    val localState = remember { derivedStateOf {
        state[types.hashCode()] ?: ArticleViewModel.State.Empty
    } }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            val currentState = localState.value
            when (currentState) {
                ArticleViewModel.State.Empty -> DesignStreamState.Default
                ArticleViewModel.State.Loading -> DesignStreamState.Loading
                is ArticleViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is ArticleViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    val pause = remember { mutableStateOf(false) }
    val showSheet = remember { mutableStateOf<UiPost?>(null) }
    val handleEvent by rememberUpdatedState(onEvent)
    val shareTitle = stringResource(eu.peernetwork.blog.ui.R.string.share_label)
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    pause.value = false
                }
                Lifecycle.Event.ON_STOP -> {
                    pause.value = true
                }
                else -> Unit
            }
        }
    }
    ArticleListing(
        id = author,
        limit = postLimit,
        state = derivedState,
        status = status,
        listState = listState,
        component = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection,
        onMenu = { showSheet.value = it },
        onClick = { handleEvent(ArticleScreenEvent.Post(it.id)) },
    ) {
        ArticleSheet(showSheet) { sheetState, post ->
            when (sheetState) {
                ArticleSheetMenuItem.BOOST -> handleEvent(ArticleScreenEvent.Boost(post.id))
                ArticleSheetMenuItem.REPORT -> it.onReport(post.id)
                ArticleSheetMenuItem.SHARE -> { context.share(post.url, shareTitle) }
            }
        }
    }
    LaunchedEffect(requireUpdate.value) {
        if (requireUpdate.value || localState.value is ArticleViewModel.State.Empty) {
            viewModel.load(
                author,
                types,
                Pageable(0, postLimit)
            )
            requireUpdate.value = false
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
