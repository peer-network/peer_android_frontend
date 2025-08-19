package eu.peernetwork.blog.ui.interactions.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.compose.ListItem
import eu.peernetwork.blog.ui.compose.ListItemSkeleton
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignErrorLabel
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ListingScreen(
    id: String,
    engagement: Engagement.Content,
    postLimit: Int,
    size: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Listing.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ListingViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                ListingViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                ListingViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is ListingViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as ListingViewModel.State.Success).content
                )
                is ListingViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as ListingViewModel.State.Error).error
                )
            }
        }
    }
    val updatedConnection by rememberUpdatedState(connection)
    DesignPagingScaffold<UiAuthor>(
        state = derivedState,
        onRefresh = {
            viewModel.load(
                id,
                size,
                engagement,
                Pageable(offset = 0, limit = postLimit)
            )
        },
        modifier = Modifier.fillMaxSize(),
        placeholder = { ListItemSkeleton(modifier = Modifier.padding(horizontal = 16.dp)) },
        errorContent = { error, refresh ->
            Column(modifier = Modifier.fillMaxSize()
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
            item { Spacer(modifier = Modifier.height(4.dp)) }
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { author ->
                    ListingScreen(
                        author = author,
                        onClick = {}
                    ) {
                        updatedConnection(
                            Triple(
                                author.id,
                                author.isfollowing,
                                author.isfollowed
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
fun ListingScreen(
    author: UiAuthor,
    onClick: (UiAuthor) -> Unit,
    action: (@Composable RowScope.() -> Unit)? = null
) {
    val slug = "#${author.slug}"
    val handleOnClick by rememberUpdatedState(onClick)
    val updatedContent by rememberUpdatedState(action)
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { handleOnClick(author) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        lead = {
            DesignAsyncImage(
                label = author.username,
                imageUrl = author.imageUrl,
                size = 42.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) {
        Row {
            Text(
                text = "@${author.username} $slug".annotate(
                    slug,
                    style = MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            updatedContent?.invoke(this)
        }
    }
}
