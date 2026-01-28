package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    listState: LazyListState,
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
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 36.dp),
        ) {
            items(
                count = items.itemCount,
                key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                items[index]?.let { transaction ->
                    val isVisible = remember { mutableStateOf(false) }
                    val isRecipient = transaction.recipient.id == uuid
                    val profile = if (isRecipient) {
                        transaction.sender
                    } else {
                        transaction.recipient
                    }
                    val title = if (isRecipient) {
                        stringResource(R.string.received_from, profile.username)
                    } else {
                        stringResource(R.string.sent_to, profile.username)
                    }
                    TransactionsItem(
                        title = transaction.res?.let { stringResource(it) }
                            ?: transactionsUsername(
                                isVisible = isVisible,
                                isRecipient = isRecipient,
                                user = profile
                            ),
                        description = transaction.message,
                        createAt = transaction.createdAt,
                        price = transaction.amount.net.toString(),
                        onClick = {},
                        leading = {
                            TransactionsAvatar(
                                icon = painterResource(R.drawable.ic_transfer_direction),
                                contentDescription = transaction.res?.let { stringResource(it) }
                                    ?: title,
                                isRecipient = isRecipient
                            ) {
                                if (transaction.icon != null && transaction.res != null) {
                                    Icon(
                                        painter = painterResource(transaction.icon),
                                        contentDescription = stringResource(transaction.res),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(8.dp)
                                    )
                                } else {
                                    TransactionsUserAvatar(
                                        user = profile,
                                        isVisible = isVisible,
                                    ) { isVisible.value = true }
                                }
                            }
                        },
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {}
                }
            }
        }
    }
}
