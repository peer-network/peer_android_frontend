package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun TransactionsList(
    limit: Int,
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    TransactionsScreen(
        limit = limit,
        lastUpdated = lastUpdated,
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, items ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 36.dp),
        ) {
            items(
                count = items.itemCount,
                key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                items[index]?.let { transaction ->
                    TransactionsItem(
                        title = stringResource(transaction.res),
                        description = transaction.message,
                        createAt = transaction.createdAt,
                        price = transaction.amount.net.toString(),
                        onClick = {},
                        leading = {},
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {}
                }
            }
        }
    }
}
