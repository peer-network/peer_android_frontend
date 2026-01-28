package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.R

@Composable
fun TransactionsList(
    uuid: String,
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
                        leading = {
                            TransactionsAvatar(
                                icon = painterResource(R.drawable.ic_transfer_direction),
                                color = MaterialTheme.colorScheme.primary,
                                contentDescription = stringResource(transaction.res),
                                isRecipient = transaction.recipient.id == uuid
                            ) {
                                Icon(
                                    painter = painterResource(transaction.icon),
                                    contentDescription = stringResource(transaction.res),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(8.dp)
                                )
                            }
                        },
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {}
                }
            }
        }
    }
}
