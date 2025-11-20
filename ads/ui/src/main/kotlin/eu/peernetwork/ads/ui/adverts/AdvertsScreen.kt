package eu.peernetwork.ads.ui.adverts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.builder

@Composable
fun AdvertsScreen(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onSelect: () -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Adverts.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = AdvertsViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            is AdvertsViewModel.State.Default -> DesignStreamState.Default
            is AdvertsViewModel.State.Loading -> DesignStreamState.Loading
            is AdvertsViewModel.State.Success -> {
                DesignStreamState.Success(
                    (state as AdvertsViewModel.State.Success).content
                )
            }
            is AdvertsViewModel.State.Error -> {
                val exception = (state as AdvertsViewModel.State.Error).error
                DesignStreamState.Error(exception)
            }
        }
    } }
    DesignPagingStream(
        state = derivedState,
        error = { error ->
            if (error.value is NoContentException) {
                AdvertsEmpty(onClick = onBack)
            } else {
                error.value.message?.let {
                    Text(component.resource().string(it))
                }
            }
        },
        loading = {
            AdvertsSkeleton(modifier = Modifier.padding(horizontal = 16.dp)
                .padding(vertical = 5.dp))
        }
    ) { lazyPagingItems ->
        AdvertScreen(
            header = content,
            onRefresh = {
                val filter = Filter().copy(author = id)
                viewModel(filter, page = Pageable(0, limit))
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index ->
                        lazyPagingItems[index]?.content?.id?.let { "$it;$index" } ?: index
                    }
                ) { index ->
                    lazyPagingItems[index]?.let { post ->
                        AdvertsItem(
                            title = post.content.title.annotate(),
                            description = post.content.description.annotate(),
                            from = post.from.toString(),
                            to = post.to.toString(),
                            status = true,
                            modifier = Modifier.padding(horizontal = 16.dp)
                                .padding(vertical = 5.dp)
                        ) {}
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        if (state is AdvertsViewModel.State.Default) {
            val filter = Filter().copy(author = id)
            viewModel(filter, page = Pageable(0, limit))
        }
    }
}

@Composable
fun AdvertScreen(
    onRefresh: () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    DesignRefreshScaffold(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        DesignScaffold(
            alwaysReturn = false,
            modifier = Modifier.fillMaxSize(),
            header = { updatedHeader() }
        ) {
            DesignScaffold(
                alwaysReturn = true,
                modifier = Modifier.fillMaxSize(),
                header = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 8.dp)
                            .padding(bottom = 10.dp)
                    ) {
                        Text(
                            "All advertisements",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            "total: 40",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            ) {  updatedContent() }
        }
    }
}
