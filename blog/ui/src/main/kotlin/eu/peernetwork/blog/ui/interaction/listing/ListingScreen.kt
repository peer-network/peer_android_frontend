package eu.peernetwork.blog.ui.interaction.listing

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.model.v2.UiAuthor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignErrorLabel
import eu.peernetwork.core.ui.design.compose.DesignPagingScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ListingScreen(
    id: String,
    engagement: Engagement.Content,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick: (String) -> Unit = {},
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
            val currentState = state["$id/$engagement"] ?: ListingViewModel.State.Empty
            when (currentState) {
                ListingViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                ListingViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is ListingViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    currentState.content
                )
                is ListingViewModel.State.Error -> DesignStatefulScaffoldState.Error(
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
                Pageable(offset = 0, limit = postLimit)
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
                    ListingScreen(
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
fun ListingScreen(
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
