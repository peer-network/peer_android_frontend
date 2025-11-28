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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.model.v2.UiAuthor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignErrorLabel
import eu.peernetwork.core.ui.design.compose.DesignPagingScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

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
    val errorMessage = stringResource(R.string.unknown_error_message)
    DesignPagingStream(
        state = derivedState,
        loading = { UserSkeleton(4) },
        error = {
            val message = it.value.message?.let { key ->
                component.resource().string(key)
            } ?: errorMessage
            UserError(message) {
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
                    UserScreen(
                        author = author,
                        onClick = { handleAuthorClick(it.id) }
                    ) {
                        updatedConnection(
                            Triple(
                                author.id,
                                author.following,
                                author.followed
                            )
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(56.dp)) }
        }
    }
}

@Composable
fun UserScreen(
    author: UiAuthor,
    onClick: (UiAuthor) -> Unit,
    action: (@Composable RowScope.() -> Unit)? = null
) {
    val slug = "#${author.slug}"
    val handleOnClick by rememberUpdatedState(onClick)
    val updatedContent by rememberUpdatedState(action)
//    ListItem(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { handleOnClick(author) }
//            .padding(vertical = 8.dp, horizontal = 16.dp),
//        lead = {
//            DesignImage(
//                label = author.username,
//                imageUrl = author.imageUrl,
//                size = 42.dp,
//                color = MaterialTheme.colorScheme.surfaceVariant,
//                style = MaterialTheme.typography.bodyMedium.copy(
//                    color = MaterialTheme.colorScheme.onBackground
//                )
//            )
//        }
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                text = "@${author.username} $slug".annotate(
//                    slug,
//                    style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
//                        color = MaterialTheme.colorScheme.tertiary
//                    )
//                ),
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.weight(1f)
//            )
//            updatedContent?.invoke(this)
//        }
//    }
}
