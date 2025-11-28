package eu.peernetwork.blog.ui.interaction.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.model.v2.UiAuthor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignErrorLabel
import eu.peernetwork.core.ui.design.compose.DesignPagingScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.error

@Composable
fun UserScreen(
    id: String,
    limit: Int,
    engagement: Engagement.Content,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (User.Component, LazyPagingItems<UiAuthor>) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(User.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            val currentState = state["$id/$engagement"] ?: UserViewModel.State.Empty
            when (currentState) {
                UserViewModel.State.Empty -> DesignStreamState.Default
                UserViewModel.State.Loading -> DesignStreamState.Loading
                is UserViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is UserViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error
                )
            }
        }
    }
    val updatedContent by rememberUpdatedState(content)
    DesignPagingStream(
        state = derivedState,
        loading = { UserSkeleton(4) },
        error = {
            UserError(component.resource().error(it.value)) {
                viewModel.load(
                    id,
                    engagement,
                    Pageable(offset = 0, limit = limit)
                )
            }
        }
    ) { updatedContent(component, it) }
    LaunchedEffect(Unit) {
        if (derivedState.value is DesignStreamState.Default) {
            viewModel.load(
                id,
                engagement,
                Pageable(offset = 0, limit = limit)
            )
        }
    }
}

@Composable
fun UserScreen(
    id: String,
    limit: Int,
    engagement: Engagement.Content,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(User.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            val currentState = state["$id/$engagement"] ?: UserViewModel.State.Empty
            when (currentState) {
                UserViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                UserViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is UserViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    currentState.content
                )
                is UserViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    currentState.error
                )
            }
        }
    }
    val updatedConnection by rememberUpdatedState(connection)
    val handleAuthorClick by rememberUpdatedState(onAuthorClick)
    DesignPagingScaffold<UiAuthor>(
        state = derivedState,
        onRefresh = {
            viewModel.load(
                id,
                engagement,
                Pageable(offset = 0, limit = limit)
            )
        },
        modifier = Modifier.fillMaxSize(),
        placeholder = { },
        errorContent = { error, refresh ->
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(8.dp))
                DesignErrorLabel(
                    onRefresh = refresh,
                    error = error,
                    resource = component.resource(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                )
            }
        }
    ) { state, lazyPagingItems ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { Spacer(modifier = Modifier.height(6.dp)) }
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { author ->
                    UserItem(
                        slug = author.slug.toString(),
                        username = author.username,
                        imageUrl = author.imageUrl,
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(56.dp)) }
        }
    }
}
