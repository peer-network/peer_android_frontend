package eu.peernetwork.wallet.ui.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.ui.model.UiTransaction

@Composable
@Suppress("UNCHECKED_CAST")
fun TransactionsScreen(
    limit: Int,
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Transactions.Component, LazyPagingItems<UiTransaction>) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Transactions.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TransactionsViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            TransactionsViewModel.State.Default -> DesignStreamState.Default
            TransactionsViewModel.State.Loading -> DesignStreamState.Loading
            is TransactionsViewModel.State.Success -> {
                val data = (state as TransactionsViewModel.State.Success)
                DesignStreamState.Success(data.content)
            }
            is TransactionsViewModel.State.Error -> {
                DesignStreamState.Error((state as TransactionsViewModel.State.Error).error)
            }
        }
    } }
    val updatedContent by rememberUpdatedState(content)
    DesignPagingStream(
        state = derivedState,
        loading = { TransactionsSkeleton() },
        error = { TransactionsError(it, component) {
            viewModel(Pageable(0, limit))
        } }
    ) { items ->
        updatedContent(component, items)
    }
    LaunchedEffect(lastUpdated.value) {
        (status as? TransactionsViewModel.Status.Success<Long>?)?.let {
            if (it.data != lastUpdated.value) {
                viewModel(Pageable(0, limit))
            }
        } ?: viewModel(Pageable(0, limit))
        viewModel.updatedAt(lastUpdated.value)
    }
}
