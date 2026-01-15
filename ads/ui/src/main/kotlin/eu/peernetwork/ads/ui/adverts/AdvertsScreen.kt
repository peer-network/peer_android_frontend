package eu.peernetwork.ads.ui.adverts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.ui.R
import eu.peernetwork.ads.ui.article.ArticleNavigator.Companion.LocalArticleNavigator
import eu.peernetwork.ads.ui.extension.route
import eu.peernetwork.ads.ui.model.UiStatus
import eu.peernetwork.ads.ui.overview.OverviewPage
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.builder

@Composable
fun AdvertsScreen(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val navigator = LocalArticleNavigator.current
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
    val handleSelect by rememberUpdatedState(onSelect)
    DesignPagingStream(
        state = derivedState,
        error = { error ->
            AdvertsError(
                error = error,
                component = component,
                onBack = onBack
            ) {
                val filter = Filter().copy(author = id)
                viewModel(filter, page = Pageable(0, limit))
            }
        },
        loading = { AdvertsSkeleton() }
    ) { lazyPagingItems ->
        AdvertScreen(
            total = lazyPagingItems.itemCount,
            header = {
                lazyPagingItems.itemSnapshotList.firstOrNull()?.let {
                    OverviewPage(it.metrics)
                }
            },
            onRefresh = {
                val filter = Filter().copy(author = id)
                viewModel(filter, page = Pageable(0, limit))
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 36.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index ->
                        lazyPagingItems[index]?.ads?.id?.let { "$it;$index" } ?: index
                    }
                ) { index ->
                    lazyPagingItems[index]?.let { post ->
                        AdvertsPost(
                            title = post.ads.content.title,
                            description = post.ads.content.description,
                            from = post.ads.from,
                            to = post.ads.to,
                            status = post.ads.status,
                            onClick = { type, value ->
                                navigator.navigate(type.route(value))
                            },
                            onSelect = { handleSelect(post.ads.id) },
                            label = if (!post.ads.content.isAccessible ||
                                post.ads.content.status != UiStatus.VISIBLE) {
                                { AdvertsVisibilityLabel() }
                            } else { null },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 5.dp)
                                .clickable { handleSelect(post.ads.id) }
                        ) { AdvertsMedia(post.ads.content.path, component) }
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
    total: Int,
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
                    Text(
                        text = stringResource(R.string.adverts_label, total)
                            .annotate(
                                text = "[$total]",
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.outline,
                                    fontSize = MaterialTheme.typography.labelMedium.fontSize
                                )
                            ),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            ) {  updatedContent() }
        }
    }
}
